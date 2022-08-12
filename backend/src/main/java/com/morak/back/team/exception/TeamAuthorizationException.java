package com.morak.back.team.exception;

import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;

public class TeamAuthorizationException extends AuthorizationException {

    public TeamAuthorizationException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static TeamAuthorizationException of(CustomErrorCode code, Long teamId, Long memberId) {
        return new TeamAuthorizationException(code, memberId + "번 멤버는 " + teamId + "번 그룹에 속해있지 않습니다.");
    }
}
