package com.morak.back.core.exception;

public class ResourceNotFoundException extends MorakException {

    public ResourceNotFoundException() {
        this("요청한 자원을 찾을 수 없습니다.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
