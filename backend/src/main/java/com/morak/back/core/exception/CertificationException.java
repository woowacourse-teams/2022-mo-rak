package com.morak.back.core.exception;

public class CertificationException extends MorakException {

    public CertificationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
