package com.morak.back.core.exception;

public class InvalidRequestException extends MorakException {

    public InvalidRequestException() {
        this("잘못된 요청입니다.");
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
