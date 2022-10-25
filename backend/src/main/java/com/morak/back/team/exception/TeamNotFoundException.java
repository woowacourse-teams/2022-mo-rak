package com.morak.back.team.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class TeamNotFoundException extends ResourceNotFoundException {

    public TeamNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static TeamNotFoundException ofTeam(CustomErrorCode code, String teamCode) {
        return new TeamNotFoundException(code, teamCode + "코드의 팀은 찾을 수 없습니다.");
    }

    public static TeamNotFoundException ofTeamInvitation(CustomErrorCode code, String invitationCode) {
        return new TeamNotFoundException(
                code, invitationCode + "초대 코드에 해당하는 팀은 찾을 수 없습니다."
        );
    }
}
