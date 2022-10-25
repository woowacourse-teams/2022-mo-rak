package com.morak.back.auth.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public class MemberDomainLogicException extends DomainLogicException {
    public MemberDomainLogicException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
