package com.morak.back.appointment.domain;

import com.morak.back.appointment.domain.menu.ClosedAt;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
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
            // TODO: 2022/10/06 fix this. 예외처리 정확하게 해야함.(커스텀 예외로)
            throw new AppointmentDomainLogicException(CustomErrorCode.TEMP_ERROR, "일반");
        }
    }
}
