package com.morak.back.appointment.domain.timeconditions.period;

import java.time.LocalDateTime;

public abstract class DateTimePeriod {

    public abstract boolean contains(DateTimePeriod other);

    public abstract LocalDateTime getLocalStartDateTime();

    public abstract LocalDateTime getLocalEndDateTime();

    @Override
    public String toString() {
        return "날짜/시간 기간: " + getLocalStartDateTime() + " ~ " + getLocalEndDateTime();
    }
}
