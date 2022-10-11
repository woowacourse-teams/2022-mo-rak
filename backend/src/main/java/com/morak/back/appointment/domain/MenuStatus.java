package com.morak.back.appointment.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;

public enum MenuStatus {
    OPEN, CLOSED;

    public Boolean isClosed() {
        return this.equals(CLOSED);
    }

    public MenuStatus close() {
        if (this == CLOSED) {
            throw new DomainLogicException(
                    CustomErrorCode.TEMP_ERROR,
                    "이미 마감된 상태입니다."
            );
        }
        return CLOSED;
    }
}
