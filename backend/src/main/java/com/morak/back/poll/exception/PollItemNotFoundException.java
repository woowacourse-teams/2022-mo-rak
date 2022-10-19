package com.morak.back.poll.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class PollItemNotFoundException extends ResourceNotFoundException {

    public PollItemNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
