package com.morak.back.appointment.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {

    private AppointmentNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static AppointmentNotFoundException ofAppointment(CustomErrorCode code, String appointmentCode) {
        return new AppointmentNotFoundException(code, "코드가 " + appointmentCode + "인 약속잡기는 찾을 수 없습니다.");
    }
}
