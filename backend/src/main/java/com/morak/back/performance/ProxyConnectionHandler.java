package com.morak.back.performance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Profile("performance")
public class ProxyConnectionHandler implements InvocationHandler {

    private final Object connection;
    private final PerformanceMonitor performanceMonitor;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = method.invoke(connection, args);
        if (isPrepareStatement(method)) {
            return Proxy.newProxyInstance(
                    returnValue.getClass().getClassLoader(),
                    returnValue.getClass().getInterfaces(),
                    new ProxyPreparedStatementHandler(returnValue, performanceMonitor)
            );
        }
        return returnValue;
    }

    private boolean isPrepareStatement(Method method) {
        return method.getName().equals("prepareStatement");
    }
}
