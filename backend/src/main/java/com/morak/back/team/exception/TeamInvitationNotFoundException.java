package com.morak.back.team.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class TeamInvitationNotFoundException extends ResourceNotFoundException {

    public TeamInvitationNotFoundException(String invitationCode) {
        super(invitationCode + "초대 코드에 해당하는 팀은 찾을 수 없습니다.");
    }
}
