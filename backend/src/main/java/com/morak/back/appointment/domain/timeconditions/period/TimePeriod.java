package com.morak.back.appointment.domain.timeconditions.period;

import java.time.LocalTime;

public abstract class TimePeriod {

    public abstract boolean contains(TimePeriod other);

    public abstract LocalTime getLocalStartTime();

    public abstract LocalTime getLocalEndTime();

    @Override
    public String toString() {
        return "시간 기간: " + getLocalStartTime() + " ~ " + getLocalEndTime();
    }
}
