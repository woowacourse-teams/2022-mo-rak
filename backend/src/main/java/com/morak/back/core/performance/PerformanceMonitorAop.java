package com.morak.back.core.performance;

import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
@Aspect
@RequiredArgsConstructor
public class PerformanceMonitorAop {

    private final PerformanceMonitor performanceMonitor;

    @Around("execution(* javax.sql.DataSource.getConnection())")
    public Object datasource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnValue = proceedingJoinPoint.proceed();
        if (isInRequestScope()) {
            return Proxy.newProxyInstance(
                    returnValue.getClass().getClassLoader(),
                    returnValue.getClass().getInterfaces(),
                    new ProxyConnectionHandler(returnValue, performanceMonitor)
            );
        }
        return returnValue;
    }

    private boolean isInRequestScope() {
        return RequestContextHolder.getRequestAttributes() != null;
    }
}
