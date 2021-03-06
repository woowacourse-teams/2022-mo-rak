package com.morak.back.poll.ui.dto;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
