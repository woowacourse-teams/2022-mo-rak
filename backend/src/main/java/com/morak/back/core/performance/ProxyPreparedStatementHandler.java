package com.morak.back.core.performance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyPreparedStatementHandler implements InvocationHandler {

    private final Object preparedStatement;
    private final PerformanceMonitor performanceMonitor;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isExecuteQuery(method)) {
            return measureQueryPerformance(method, args);
        }
        return method.invoke(preparedStatement, args);
    }

    private boolean isExecuteQuery(Method method) {
        return method.getName().equals("executeQuery");
    }

    private Object measureQueryPerformance(Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = method.invoke(preparedStatement, args);
        performanceMonitor.addQueryTime(System.currentTimeMillis() - startTime);
        performanceMonitor.increaseQueryCount();
        return returnValue;
    }
}
