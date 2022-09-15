package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import com.morak.back.appointment.domain.appointment.timeconditions.period.TimePeriod;
import java.time.LocalTime;

public class AvailableTimeTimePeriod extends TimePeriod {

    private final LocalTime startTime;
    private final LocalTime endTime;

    protected AvailableTimeTimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean contains(TimePeriod other) {
        return false;
    }

    @Override
    public LocalTime getLocalStartTime() {
        return startTime;
    }

    @Override
    public LocalTime getLocalEndTime() {
        return endTime;
    }
}
