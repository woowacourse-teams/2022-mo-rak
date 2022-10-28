package com.morak.back.notification.domain.slack;

public interface SlackClient {

    void notifyMessage(SlackWebhook webhook, String message);
}
