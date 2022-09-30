package com.morak.back.brandnew;

import java.time.LocalDateTime;

public class RealDateTime implements MorakDateTime {

    private final LocalDateTime dateTime;

    public RealDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


    @Override
    public boolean isBeforeNow() {
        return dateTime.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isAfterNow() {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
