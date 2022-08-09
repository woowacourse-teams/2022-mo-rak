package com.morak.back.core.ui;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.morak.back.auth.exception.AuthenticationException;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CachedBodyException;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import com.morak.back.core.exception.MorakException;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.core.support.LogFormatter;
import com.morak.back.poll.ui.dto.ExceptionResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(DomainLogicException.class)
    public ResponseEntity<ExceptionResponse> handleDomainLogic(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse.from(e.getCode()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthentication(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ExceptionResponse.from(e.getCode()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorization(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ExceptionResponse.from(e.getCode()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse.from(e.getCode()));
    }

    //TODO MethodArgumentValidateException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException e) {
        logger.warn(e.getMessage());

        String violationMessage = createViolationMessage(e.getConstraintViolations());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber(), violationMessage));
    }

    @ExceptionHandler({MismatchedInputException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleMismatchedInput(ConstraintViolationException e) {
        // TODO : check this
        logger.warn(e.getMessage());

        String violationMessage = createViolationMessage(e.getConstraintViolations());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber(), violationMessage));
    }


    private String createViolationMessage(Set<ConstraintViolation<?>> constraintViolations) {
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            System.out.println("constraintViolation.getMessage() = " + constraintViolation.getMessage());
            System.out.println("constraintViolation.getMessageTemplate() = " + constraintViolation.getMessageTemplate());
        }
        return "";
    }

    //TODO
    @ExceptionHandler(CachedBodyException.class)
    public ResponseEntity<ExceptionResponse> handleCachedBodyException(CachedBodyException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse.from(CustomErrorCode.CACHED_BODY_ERROR));
    }

    @ExceptionHandler(MorakException.class)
    public ResponseEntity<ExceptionResponse> handleMorak(MorakException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse.from(CustomErrorCode.MORAK_ERROR));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUndefined(RuntimeException e, HttpServletRequest request)
        throws IOException {
        String stackTrace = Arrays.stream(e.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining(" <- "));
        logger.error(stackTrace + LogFormatter.toPrettyRequestString(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse.from(CustomErrorCode.RUNTIME_ERROR));
    }
}
