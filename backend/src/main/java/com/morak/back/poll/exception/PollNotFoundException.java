package com.morak.back.poll.exception;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ResourceNotFoundException;

public class PollNotFoundException extends ResourceNotFoundException {

    public PollNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static PollNotFoundException ofPoll(CustomErrorCode code, String pollCode) {
        return new PollNotFoundException(code, pollCode + " 코드에 해당하는 투표를 찾을 수 없습니다.");
    }

    public static PollNotFoundException ofPollItem(CustomErrorCode code, Long pollItemId) {
        return new PollNotFoundException(code, pollItemId + "번 투표 항목은 찾을 수 없습니다.");
    }
}
