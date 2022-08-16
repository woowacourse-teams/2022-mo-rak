package com.morak.back.core.ui;

import com.morak.back.auth.exception.AuthenticationException;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import com.morak.back.core.exception.MorakException;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.core.support.LogFormatter;
import com.morak.back.core.ui.dto.ExceptionResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(DomainLogicException.class)
    public ResponseEntity<ExceptionResponse> handleDomainLogic(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getCode().getNumber(), "잘못된 요청입니다."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthentication(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getCode().getNumber(), "사용자 인증에 실패했습니다."));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorization(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(e.getCode().getNumber(), "접근 권한이 없는 요청입니다."));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getCode().getNumber(), "요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException e) {
        logger.warn(e.getMessage());

        String messages = extractErrorMessages(e.getConstraintViolations());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber(), messages));
    }

    private String extractErrorMessages(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMismatchedInput(MethodArgumentNotValidException e) {
        logger.warn(e.getMessage());

        String messages = extractErrorMessages(e.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber(), messages));
    }

    private String extractErrorMessages(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleMismatchedInput(Exception e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber(), "잘못된 요청입니다."));
    }

    @ExceptionHandler(MorakException.class)
    public ResponseEntity<ExceptionResponse> handleMorak(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(CustomErrorCode.MORAK_ERROR.getNumber(), "처리하지 못한 예외입니다."));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> noHandlerFoundHandle(NoHandlerFoundException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(CustomErrorCode.API_NOT_FOUND_ERROR.getNumber(), "처리할 수 없는 요청입니다."));
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUndefined(RuntimeException e,
                                                             ContentCachingRequestWrapper requestWrapper) {
        String stackTrace = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(" <- "));
        logger.error(stackTrace + LogFormatter.toPrettyRequestString(requestWrapper));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(CustomErrorCode.RUNTIME_ERROR.getNumber(), "알 수 없는 예외입니다."));
    }
}
