package com.morak.back.core.exception;

public class DomainLogicException extends MorakException {

    public DomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
