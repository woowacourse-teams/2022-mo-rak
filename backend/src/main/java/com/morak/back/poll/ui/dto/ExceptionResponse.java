package com.morak.back.poll.ui.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExceptionResponse {

    private final String codeNumber;
    private final String message;
}
