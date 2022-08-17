package com.morak.back.core.exception;

public class ResourceNotFoundException extends MorakException {

    public ResourceNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
