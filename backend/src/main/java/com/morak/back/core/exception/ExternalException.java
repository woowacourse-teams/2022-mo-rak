package com.morak.back.core.exception;

public class ExternalException extends MorakException {

    public ExternalException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
