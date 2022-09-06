package com.morak.back.core.performance;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class QueryMonitor {

    private int count;
    private long time;
    private boolean activate;

    public QueryMonitor() {
        activate = false;
    }

    public void start() {
        activate = true;
        count = 0;
    }

    public void countUp() {
        if (activate) {
            count++;
        }
    }

    public void addTime(long time) {
        if (activate) {
            this.time += time;
        }
    }

    public void end() {
        activate = false;
    }
}
