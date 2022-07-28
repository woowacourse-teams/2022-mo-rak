package com.morak.back.appointment.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {

    public AppointmentNotFoundException(String appointmentCode) {
        super("코드가 " + appointmentCode + "인 약속잡기는 찾을 수 없습니다.");
    }
}
