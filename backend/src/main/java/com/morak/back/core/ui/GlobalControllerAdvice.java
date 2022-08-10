package com.morak.back.core.ui;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.core.exception.MorakException;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.core.support.LogFormatter;
import com.morak.back.poll.ui.dto.ExceptionResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Order(2)
@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler({MismatchedInputException.class, InvalidRequestException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleMismatchedInput(MorakException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("잘못된 요청입니다."));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse("요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(ConstraintViolationException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("잘못된 값이 입력되었습니다."));
    }

    @ExceptionHandler(MorakException.class)
    public ResponseEntity<ExceptionResponse> handleMorakException(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("처리하지 못한 예외입니다."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUndefined(RuntimeException e, ContentCachingRequestWrapper requestWrapper) {
        String stackTrace = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(" <- "));
        logger.error(stackTrace + LogFormatter.toPrettyRequestString(requestWrapper));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse("알 수 없는 에러입니다."));
    }
}
