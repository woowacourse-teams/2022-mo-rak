package com.morak.back.role.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class RoleNotFoundException extends ResourceNotFoundException {

    public RoleNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static RoleNotFoundException ofTeam(CustomErrorCode code, String teamCode) {
        return new RoleNotFoundException(code, teamCode + " 코드의 팀에 속한 역할은 찾을 수 없습니다.");
    }
}
