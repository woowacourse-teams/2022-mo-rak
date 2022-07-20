package com.morak.back.poll.exception;

public class ResourceNotFoundException extends PollException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
