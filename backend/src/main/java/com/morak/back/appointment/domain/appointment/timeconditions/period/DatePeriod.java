package com.morak.back.appointment.domain.appointment.timeconditions.period;

import java.time.LocalDate;

public abstract class DatePeriod {

    public abstract LocalDate getLocalStartDate();

    public abstract LocalDate getLocalEndDate();

    @Override
    public String toString() {
        return "날짜 기간: " + getLocalStartDate() + " ~ " + getLocalEndDate();
    }
}
