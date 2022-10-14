package com.morak.back.role.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;

public class RoleName {

    private static final int MAX_LENGTH = 20;

    private final String value;

    public RoleName(String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new RoleDomainLogicException(
                    CustomErrorCode.INVALID_ROLE_NAME_LENGTH_ERROR,
                    value + "는 " + MAX_LENGTH + "자를 넘을 수 없습니다."
            );
        }
    }
}
