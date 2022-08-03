package com.morak.back.core.ui;

import com.morak.back.core.exception.ExternalException;
import com.morak.back.core.exception.MorakException;
import com.morak.back.poll.ui.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(ExternalException.class)
    public ResponseEntity<ExceptionResponse> handleExternalFailure(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ExceptionResponse("외부 API 요청을 실패했습니다."));
    }
}
