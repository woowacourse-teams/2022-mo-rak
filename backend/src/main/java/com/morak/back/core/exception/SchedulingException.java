package com.morak.back.core.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class SchedulingException extends MorakException {

    private List<ExternalException> exceptions;

    public SchedulingException(CustomErrorCode code, String logMessage, List<ExternalException> exceptions) {
        super(code, logMessage);
        this.exceptions = exceptions;
    }
}
