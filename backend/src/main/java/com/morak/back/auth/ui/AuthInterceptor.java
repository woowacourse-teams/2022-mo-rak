package com.morak.back.auth.ui;

import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.exception.AuthorizationException;
import com.morak.back.auth.support.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request)) {
            return true;
        }

        String token = extractToken(request);
        tokenProvider.validateToken(token);

        return true;
    }

    private boolean isPreflight(final HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private String extractToken(HttpServletRequest request) {
        try {
            return AuthorizationExtractor.extractOrThrow(request);
        } catch (IllegalArgumentException e) {
            throw new AuthorizationException("요청에서 토큰을 추출하는데 실패했습니다.");
        }
    }
}
