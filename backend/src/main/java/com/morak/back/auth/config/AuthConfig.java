package com.morak.back.auth.config;

import com.morak.back.auth.ui.AuthInterceptor;
import com.morak.back.auth.ui.AuthResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthResolver authResolver;

    public AuthConfig(AuthInterceptor authInterceptor, AuthResolver authResolver) {
        this.authInterceptor = authInterceptor;
        this.authResolver = authResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/groups/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authResolver);
    }
}
