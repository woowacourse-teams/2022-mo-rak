package com.morak.back.poll.ui;

import com.morak.back.poll.exception.InvalidRequestException;
import com.morak.back.poll.exception.ResourceNotFoundException;
import com.morak.back.poll.ui.dto.ExceptionResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PollControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(RuntimeException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse("요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(RuntimeException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("잘못된 요청입니다."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUndefined(RuntimeException e) {
        String stackTrace = Arrays.stream(e.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n"));
        logger.error(stackTrace);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ExceptionResponse("알 수 없는 에러입니다."));
    }
}
