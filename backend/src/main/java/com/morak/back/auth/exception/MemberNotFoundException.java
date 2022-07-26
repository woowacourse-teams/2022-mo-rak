package com.morak.back.auth.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(Long memberId) {
        super(memberId + "번 멤버는 찾을 수 없습니다.");
    }
}
