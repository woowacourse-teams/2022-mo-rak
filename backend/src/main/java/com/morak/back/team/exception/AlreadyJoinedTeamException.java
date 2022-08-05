package com.morak.back.team.exception;

public class AlreadyJoinedTeamException extends TeamException {

    public AlreadyJoinedTeamException() {
        super("팀에 이미 속해있습니다.");
    }
}
