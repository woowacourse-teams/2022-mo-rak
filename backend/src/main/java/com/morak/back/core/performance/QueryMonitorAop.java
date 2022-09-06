package com.morak.back.core.performance;

import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class QueryMonitorAop {

    private final QueryMonitor queryMonitor;

    @Around("execution(* javax.sql.DataSource.getConnection())")
    public Object datasource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnValue = proceedingJoinPoint.proceed();
        return Proxy.newProxyInstance(
                returnValue.getClass().getClassLoader(),
                returnValue.getClass().getInterfaces(),
                new ProxyConnectionHandler(returnValue, queryMonitor)
        );
    }
}
