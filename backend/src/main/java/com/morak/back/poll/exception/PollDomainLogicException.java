package com.morak.back.poll.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public class PollDomainLogicException extends DomainLogicException {

    public PollDomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
