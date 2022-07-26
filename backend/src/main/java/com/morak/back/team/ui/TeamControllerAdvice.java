package com.morak.back.team.ui;

import com.morak.back.poll.ui.dto.ExceptionResponse;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import com.morak.back.team.exception.TeamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = TeamController.class)
public class TeamControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler({MismatchedTeamException.class, AlreadyJoinedTeamException.class})
    public ResponseEntity<ExceptionResponse> handleInappropriateTeamMember(TeamException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("접근 권한이 없는 요청입니다."));
    }

    @ExceptionHandler(ExpiredInvitationException.class)
    public ResponseEntity<ExceptionResponse> handleExpired(TeamException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse("잘못된 요청입니다."));
    }

    @ExceptionHandler(TeamException.class)
    public ResponseEntity<ExceptionResponse> handleTeamException(TeamException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("처리하지 못한 팀 예외입니다."));
    }
}
