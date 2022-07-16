package com.morak.back.core.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.morak.back.poll.ui.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ExceptionResponse> handleUndefined(RuntimeException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("잘못된 요청입니다."));
    }
}
