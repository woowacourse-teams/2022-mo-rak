package com.morak.back.team.ui;

import com.morak.back.poll.ui.dto.ExceptionResponse;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TeamControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler({MismatchedTeamException.class, AlreadyJoinedTeamException.class})
    public ResponseEntity<ExceptionResponse> handleInappropriateTeamMember(RuntimeException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(ExpiredInvitationException.class)
    public ResponseEntity<ExceptionResponse> handleExpired(RuntimeException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }
}
