package com.morak.back.core.performance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyConnectionHandler implements InvocationHandler {

    private final Object connection;
    private final QueryMonitor queryMonitor;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = method.invoke(connection, args);
        if (isPrepareStatement(method)) {
            return Proxy.newProxyInstance(
                    returnValue.getClass().getClassLoader(),
                    returnValue.getClass().getInterfaces(),
                    new ProxyPreparedStatementHandler(returnValue, queryMonitor)
            );
        }
        return returnValue;
    }

    private boolean isPrepareStatement(Method method) {
        return method.getName().equals("prepareStatement");
    }
}
