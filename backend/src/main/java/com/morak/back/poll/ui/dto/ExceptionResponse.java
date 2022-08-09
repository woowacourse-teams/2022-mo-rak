package com.morak.back.poll.ui.dto;

import com.morak.back.core.exception.CustomErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExceptionResponse {

    private final String codeNumber;
    private final String message;

    public static ExceptionResponse from(CustomErrorCode code) {
        return new ExceptionResponse(code.getNumber(), code.getInformation());
    }
}
