package com.morak.back.core.exception;

public class AuthorizationException extends CertificationException {

    public AuthorizationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
