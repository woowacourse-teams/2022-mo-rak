package com.morak.back.team.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public class TeamDomainLogicException extends DomainLogicException {

    public TeamDomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
