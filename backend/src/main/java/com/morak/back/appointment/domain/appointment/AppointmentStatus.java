package com.morak.back.appointment.domain.appointment;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;

public enum AppointmentStatus {

    OPEN, CLOSED;

    public Boolean isClosed() {
        return this.equals(CLOSED);
    }

    public AppointmentStatus close() {
        if (this == CLOSED) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR,
                    "이미 close 상태의 약속잡기입니다."
            );
        }
        return CLOSED;
    }
}
