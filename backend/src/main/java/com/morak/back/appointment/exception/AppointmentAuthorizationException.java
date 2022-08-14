package com.morak.back.appointment.exception;

import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;

public class AppointmentAuthorizationException extends AuthorizationException {

    public AppointmentAuthorizationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
