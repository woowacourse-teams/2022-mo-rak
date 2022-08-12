package com.morak.back.core.exception;

public class CachedBodyException extends MorakException {

    public CachedBodyException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
