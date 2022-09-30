package com.morak.back.brandnew;

import java.time.LocalDateTime;
import lombok.Builder;

public class FakeDateTime implements MorakDateTime {

    private final LocalDateTime dateTime;
    private final LocalDateTime now;

    @Builder
    public FakeDateTime(LocalDateTime dateTime, LocalDateTime now) {
        this.dateTime = dateTime;
        this.now = now;
    }

    @Override
    public boolean isBeforeNow() {
        return dateTime.isBefore(now);
    }

    @Override
    public boolean isAfterNow() {
        return dateTime.isAfter(now);
    }
}
