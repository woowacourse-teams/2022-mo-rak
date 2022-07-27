package com.morak.back.appointment.domain;

public enum AppointmentStatus {

    OPEN, CLOSED;

    public Boolean isClosed() {
        return this.equals(CLOSED);
    }
}
