package com.morak.back.poll.domain;

public enum PollStatus {
    OPEN, CLOSED;

    public boolean isClosed() {
        return this.equals(CLOSED);
    }
}
