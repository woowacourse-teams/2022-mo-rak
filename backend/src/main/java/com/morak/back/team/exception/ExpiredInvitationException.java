package com.morak.back.team.exception;

public class ExpiredInvitationException extends TeamException {

    public ExpiredInvitationException() {
        super("이미 만료된 초대 코드입니다.");
    }
}
