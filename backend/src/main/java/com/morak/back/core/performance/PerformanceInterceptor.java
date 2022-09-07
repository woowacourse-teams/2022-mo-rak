package com.morak.back.core.performance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Profile("test")
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger("PERFORMANCE");

    private final PerformanceMonitor performanceMonitor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request)) {
            return true;
        }
        performanceMonitor.setUri(request.getRequestURI());
        performanceMonitor.setMethod(request.getMethod());
        performanceMonitor.setRequestTime(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        if (isPreflight(request) || performanceMonitor.getUri() == null) {
            return;
        }
        performanceMonitor.setRequestTime(System.currentTimeMillis() - performanceMonitor.getRequestTime());
        performanceMonitor.setStatus(response.getStatus());

        log.info(performanceMonitor.toString());
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }
}
