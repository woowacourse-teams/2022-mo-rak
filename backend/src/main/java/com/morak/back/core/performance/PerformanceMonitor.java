package com.morak.back.core.performance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class PerformanceMonitor {

    private String uri;
    private String method;
    private int status;
    private long requestTime;
    private int queryCount;
    private long queryTime;

    public void increaseQueryCount() {
        queryCount++;
    }

    public void addQueryTime(long queryTime) {
        this.queryTime += queryTime;
    }

    @Override
    public String toString() {
        return String.format(
                "uri: '%s' method: '%s' status: %d 요청처리시간: %d ms 쿼리개수: %d 쿼리시간: %d ms",
                uri,
                method,
                status,
                requestTime,
                queryCount,
                queryTime
        );
    }
}
