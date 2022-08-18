package com.morak.back.core.support;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.ContentCachingRequestWrapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        stringBuilder.append(new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));
        return stringBuilder;
    }
}
