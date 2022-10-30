package com.morak.back.appointment.domain;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.exception.CustomErrorCode;
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
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR,
                    closedAt + "은 " + endDateTime + "보다 과거여야 합니다."
            );
        }
    }
}
