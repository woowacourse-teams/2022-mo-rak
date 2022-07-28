package com.morak.back.poll.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class PollNotFoundException extends ResourceNotFoundException {

    public PollNotFoundException(String pollCode, Long teamId) {
        this(teamId + "번 팀에 속한 " + pollCode + "코드 투표는 찾을 수 없습니다.");
    }

    public PollNotFoundException(String message) {
        super(message);
    }
}
