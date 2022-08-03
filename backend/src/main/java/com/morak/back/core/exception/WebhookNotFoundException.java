package com.morak.back.core.exception;

public class WebhookNotFoundException extends ResourceNotFoundException {

    public WebhookNotFoundException(Long teamId) {
        super(teamId + "번 팀ID에 해당하는 웹훅이 없습니다.");
    }
}
