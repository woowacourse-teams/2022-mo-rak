package com.morak.back.core.domain.slack;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class FakeSlackClient implements SlackClient {

    @Override
    public void notifyClosed(SlackWebhook webhook, String message) {
        System.out.println("슬랙 API를 요청합니다\n" +
                "url = " + webhook.getUrl() + "\n" +
            "message = " + message);
    }
}
