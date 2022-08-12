package com.morak.back.auth.exception;

import com.morak.back.core.exception.CertificationException;
import com.morak.back.core.exception.CustomErrorCode;

public class AuthenticationException extends CertificationException {

    public AuthenticationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
