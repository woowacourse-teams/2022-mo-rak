package com.morak.back.core.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class LogFormatter {

    private static final String NEWLINE = System.getProperty("line.separator");

    public static String toPrettyRequestString(HttpServletRequest httpRequest) throws IOException {
        return NEWLINE + getMethodAndURI(httpRequest)
                + getHeaders(httpRequest)
                + getBody(httpRequest);
    }

    private static StringBuilder getHeaders(HttpServletRequest httpRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            stringBuilder.append(headerName).append(": ").append(httpRequest.getHeader(headerName)).append(NEWLINE);
        }
        return stringBuilder;
    }

    private static StringBuilder getMethodAndURI(HttpServletRequest httpRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("method: ").append(httpRequest.getMethod()).append(NEWLINE);
        stringBuilder.append("uri: ").append(httpRequest.getRequestURI()).append(NEWLINE);
        return stringBuilder;
    }

    private static StringBuilder getBody(HttpServletRequest httpRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = httpRequest.getReader();
        stringBuilder.append("body: ").append(NEWLINE);
        stringBuilder.append(reader.lines().collect(Collectors.joining(NEWLINE)));
        return stringBuilder;
    }
}
