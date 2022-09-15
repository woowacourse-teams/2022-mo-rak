package com.morak.back.performance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Profile("test")
public class ProxyPreparedStatementHandler implements InvocationHandler {

    private final Object preparedStatement;
    private final PerformanceMonitor performanceMonitor;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isExecute(method)) {
            return measureQueryPerformance(method, args);
        }
        return method.invoke(preparedStatement, args);
    }

    private boolean isExecute(Method method) {
        return method.getName().contains("execute");
    }

    private Object measureQueryPerformance(Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();
        Object returnValue = method.invoke(preparedStatement, args);
        performanceMonitor.addQueryTime(System.nanoTime() - startTime);
        performanceMonitor.increaseQueryCount();
        return returnValue;
    }
}
