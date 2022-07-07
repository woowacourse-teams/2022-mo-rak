package com.morak.back.poll.domain;

public enum PollStatus {
    OPEN, CLOSED;

    public boolean isClosed() {
        return this.equals(CLOSED);
    }

    public PollStatus close() {
        if (this == CLOSED) {
            throw new IllegalArgumentException();
        }
        return CLOSED;
    }
}
