package com.morak.back.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentClosedAt extends ClosedAt {
    
    public AppointmentClosedAt(LocalDateTime closedAt, LocalDateTime now, LocalDate endDate, LocalTime endTime) {
        super(closedAt, now);
        LocalDateTime endDateTime = getLocalDateTime(endDate, endTime);
        validatePastEndDateTime(closedAt, endDateTime);
    }

    private LocalDateTime getLocalDateTime(LocalDate endDate, LocalTime endTime) {
        if (endTime.equals(LocalTime.MIDNIGHT)) {
            endDate = endDate.plusDays(1);
        }
        return LocalDateTime.of(endDate, endTime);
    }

    private void validatePastEndDateTime(LocalDateTime closedAt, LocalDateTime endDateTime) {
        if (closedAt.isAfter(endDateTime)) {
            throw new IllegalArgumentException(); // TODO: 2022/10/06 fix this 
        }
    }
}
