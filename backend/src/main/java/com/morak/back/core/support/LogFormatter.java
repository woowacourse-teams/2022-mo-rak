package com.morak.back.core.support;

import java.util.Arrays;
import java.util.Enumeration;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class LogFormatter {

    private static final String NEWLINE = System.getProperty("line.separator");

    public static String toPrettyRequestString(ContentCachingRequestWrapper requestWrapper) {
        return NEWLINE + getMethodAndURI(requestWrapper)
                + getHeaders(requestWrapper)
                + getBody(requestWrapper);
    }

    private static StringBuilder getHeaders(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            stringBuilder.append(headerName).append(": ").append(requestWrapper.getHeader(headerName)).append(NEWLINE);
        }
        return stringBuilder;
    }

    private static StringBuilder getMethodAndURI(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("method: ").append(requestWrapper.getMethod()).append(NEWLINE);
        stringBuilder.append("uri: ").append(requestWrapper.getRequestURI()).append(NEWLINE);
        return stringBuilder;
    }

    private static StringBuilder getBody(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("body: ").append(NEWLINE);
        stringBuilder.append(Arrays.toString(requestWrapper.getContentAsByteArray()));
        return stringBuilder;
    }
}
