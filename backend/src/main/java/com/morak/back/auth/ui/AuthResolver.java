package com.morak.back.auth.ui;

import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.exception.AuthorizationException;
import com.morak.back.auth.support.Auth;
import com.morak.back.auth.support.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    public AuthResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = extractToken(request);
        String payload = tokenProvider.parsePayload(token);
        return Long.parseLong(payload);
    }

    private String extractToken(HttpServletRequest request) {
        try {
            return AuthorizationExtractor.extractOrThrow(request);
        } catch (IllegalArgumentException e) {
            throw new AuthorizationException("요청에서 토큰을 추출하는데 실패했습니다.");
        }
    }
}
