package com.morak.back.auth.ui;

import com.morak.back.auth.application.AuthService;
import com.morak.back.auth.exception.AuthorizationException;
import com.morak.back.auth.support.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request)) {
            return true;
        }

        String token = getToken(request);
        Long memberId = authService.getMemberId(token);
        authService.validateMemberId(memberId);

        request.setAttribute("memberId", memberId);

        return true;
    }

    private String getToken(HttpServletRequest request) {
        try {
            return AuthorizationExtractor.extractOrThrow(request);
        } catch (IllegalArgumentException e) {
            throw new AuthorizationException("요청에서 토큰을 추출하는데 실패했습니다.");
        }
    }

    private boolean isPreflight(final HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}
