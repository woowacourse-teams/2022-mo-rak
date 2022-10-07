package com.morak.back.poll.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;

public enum PollStatus {

    OPEN, CLOSED;

    public boolean isClosed() {
        return this.equals(CLOSED);
    }

    public PollStatus close() {
        if (this == CLOSED) {
            throw new PollDomainLogicException(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR, "이미 종료된 투표입니다.");
        }
        return CLOSED;
    }
}
