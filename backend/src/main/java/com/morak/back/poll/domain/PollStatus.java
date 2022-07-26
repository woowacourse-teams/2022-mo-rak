package com.morak.back.poll.domain;

import com.morak.back.core.exception.InvalidRequestException;

public enum PollStatus {
    OPEN, CLOSED;

    public boolean isClosed() {
        return this.equals(CLOSED);
    }

    public PollStatus close() {
        if (this == CLOSED) {
            throw new InvalidRequestException("이미 close 상태의 투표입니다.");
        }
        return CLOSED;
    }
}
