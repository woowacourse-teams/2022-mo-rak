package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;

public enum AppointmentStatus {

    OPEN, CLOSED;

    public Boolean isClosed() {
        return this.equals(CLOSED);
    }

    public AppointmentStatus close() {
        if (this == CLOSED) {
            throw new InvalidRequestException("이미 close 상태의 약속잡기입니다.");
        }
        return CLOSED;
    }
}
