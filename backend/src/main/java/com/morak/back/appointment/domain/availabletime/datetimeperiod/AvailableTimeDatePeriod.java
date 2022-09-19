package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import com.morak.back.appointment.domain.appointment.timeconditions.period.DatePeriod;
import java.time.LocalDate;

public class AvailableTimeDatePeriod extends DatePeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;

    protected AvailableTimeDatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public LocalDate getLocalStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getLocalEndDate() {
        return endDate;
    }
}
