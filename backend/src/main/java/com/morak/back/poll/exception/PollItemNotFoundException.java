package com.morak.back.poll.exception;

import com.morak.back.core.exception.ResourceNotFoundException;

public class PollItemNotFoundException extends ResourceNotFoundException {

    public PollItemNotFoundException(Long pollItemId) {
        this(pollItemId + "번 투표 항목은 찾을 수 없습니다.");
    }

    public PollItemNotFoundException(String message) {
        super(message);
    }
}
