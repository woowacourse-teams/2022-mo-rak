package com.morak.back.role.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public class RoleDomainLogicException extends DomainLogicException {

    public RoleDomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
