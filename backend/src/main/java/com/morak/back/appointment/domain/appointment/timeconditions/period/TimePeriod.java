package com.morak.back.appointment.domain.appointment.timeconditions.period;

import java.time.LocalTime;

public abstract class TimePeriod {

    public abstract LocalTime getLocalStartTime();

    public abstract LocalTime getLocalEndTime();

    @Override
    public String toString() {
        return "시간 기간: " + getLocalStartTime() + " ~ " + getLocalEndTime();
    }
}
