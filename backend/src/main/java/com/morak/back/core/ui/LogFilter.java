package com.morak.back.core.ui;

import com.morak.back.core.support.LogFormatter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: 2022/08/02 동시성 문제가 있을 수 있어 현재 ControllerAdvice에서 처리하고 있습니다.
public class LogFilter implements Filter {

    private static final int SERVER_ERROR_CODE = 500;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpResponse.getStatus() >= SERVER_ERROR_CODE) {
            logger.error(LogFormatter.toPrettyRequestString((HttpServletRequest) request));
        }
    }
}
