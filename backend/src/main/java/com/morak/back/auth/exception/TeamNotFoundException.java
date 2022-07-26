package com.morak.back.auth.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class TeamNotFoundException extends ResourceNotFoundException {

    public TeamNotFoundException(String teamCode) {
        super(teamCode + "코드의 팀은 찾을 수 없습니다.");
    }
}
