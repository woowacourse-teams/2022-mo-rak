package com.morak.back.core.exception;

import lombok.Getter;

@Getter
public class MorakException extends RuntimeException {

    private final CustomErrorCode code;

    public MorakException(CustomErrorCode code, String logMessage) {
        super(logMessage);
        this.code = code;
    }
}
