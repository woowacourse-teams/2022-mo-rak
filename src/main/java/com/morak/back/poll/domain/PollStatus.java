package com.morak.back.poll.domain;

import com.morak.back.poll.exception.InvalidRequestException;

public enum PollStatus {
    OPEN, CLOSED;

    public boolean isClosed() {
        return this.equals(CLOSED);
    }

    public PollStatus close() {
        if (this == CLOSED) {
            throw new InvalidRequestException();
        }
        return CLOSED;
    }
}
