package com.morak.back.core.domain.slack;

public interface SlackClient {

    void notifyMessage(SlackWebhook webhook, String message);
}
