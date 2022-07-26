package com.morak.back.team.exception;

public class MismatchedTeamException extends TeamException {

    public MismatchedTeamException(Long teamId, Long memberId) {
        this(memberId + "번 멤버는 " + teamId + "번 그룹에 속해있지 않습니다.");
    }

    public MismatchedTeamException(String message) {
        super(message);
    }
}
