package com.morak.back.appointment.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public class AppointmentDomainLogicException extends DomainLogicException {

    public AppointmentDomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
