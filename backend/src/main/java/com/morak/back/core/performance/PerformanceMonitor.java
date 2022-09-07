package com.morak.back.core.performance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
@Profile("test")
public class PerformanceMonitor {

    private String uri;
    private String method;
    private double requestTime;
    private int queryCount;
    private double queryTime;

    public void increaseQueryCount() {
        queryCount++;
    }

    public void addQueryTime(long queryTime) {
        this.queryTime += queryTime;
    }

    @Override
    public String toString() {
        return String.format(
                "uri: '%s', method: '%s', 요청 처리 시간: %f ms, 쿼리 개수: %d, 쿼리 시간: %f ms",
                uri,
                method,
                requestTime / 1_000_000.0,
                queryCount,
                queryTime / 1_000_000.0
        );
    }
}
