package com.morak.back.auth.exception;

import com.morak.back.core.exception.MorakException;

public class AuthorizationException extends MorakException {

    public AuthorizationException(String message) {
        super(message);
    }
}
