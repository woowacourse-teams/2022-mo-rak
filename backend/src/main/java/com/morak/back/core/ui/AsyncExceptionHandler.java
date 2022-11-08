package com.morak.back.core.ui;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.warn("비동기 처리중 예외가 발생했습니다\n" +
                "예외 메세지 : " + ex.getMessage() + "\n" +
                "메소드 : " + method.getName() + "\n" +
                "파라미터 : " + Arrays.toString(params));
    }
}
