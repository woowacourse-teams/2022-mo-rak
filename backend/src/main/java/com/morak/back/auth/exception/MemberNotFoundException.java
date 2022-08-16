package com.morak.back.auth.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    private MemberNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static MemberNotFoundException of(CustomErrorCode code, Long memberId) {
        return new MemberNotFoundException(code, memberId + "번 멤버는 찾을 수 없습니다.");
    }
}
