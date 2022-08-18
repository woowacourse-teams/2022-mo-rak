package com.morak.back.core.exception;

import com.morak.back.core.support.Generated;

@Generated
public class ExternalException extends MorakException {

    public ExternalException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
