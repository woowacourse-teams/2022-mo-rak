package com.morak.back.poll.exception;

import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;

public class PollAuthorizationException extends AuthorizationException {

    public PollAuthorizationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
