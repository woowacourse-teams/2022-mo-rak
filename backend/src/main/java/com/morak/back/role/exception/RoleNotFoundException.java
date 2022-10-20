package com.morak.back.role.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class RoleNotFoundException extends ResourceNotFoundException {

    public RoleNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
