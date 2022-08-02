package com.morak.back.core.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class LogFormatter {

    private static final String NEWLINE = System.getProperty("line.separator");

    public static String toPrettyRequestString(HttpServletRequest httpRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(NEWLINE);
        appendMethodAndURI(httpRequest, stringBuilder);
        appendHeaders(httpRequest, stringBuilder);
        appendBody(httpRequest, stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendBody(HttpServletRequest httpRequest, StringBuilder stringBuilder) throws IOException {
        BufferedReader reader = httpRequest.getReader();
        stringBuilder.append("body: ").append(NEWLINE);
        reader.lines().forEach(line -> stringBuilder.append(line).append(NEWLINE));
    }

    private static void appendHeaders(HttpServletRequest httpRequest, StringBuilder stringBuilder) {
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            stringBuilder.append(headerName).append(": ").append(httpRequest.getHeader(headerName)).append(NEWLINE);
        }
    }

    private static void appendMethodAndURI(HttpServletRequest httpRequest, StringBuilder stringBuilder) {
        stringBuilder.append("method: ").append(httpRequest.getMethod()).append(NEWLINE);
        stringBuilder.append("uri: ").append(httpRequest.getRequestURI()).append(NEWLINE);
    }
}
