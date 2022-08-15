package com.morak.back.core.exception;

public class WebhookNotFoundException extends ResourceNotFoundException {

    private WebhookNotFoundException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }

    public static WebhookNotFoundException of(CustomErrorCode code, Long teamId) {
        return new WebhookNotFoundException(code, teamId + "번 팀ID에 해당하는 웹훅이 없습니다.");
    }
}
