package com.morak.back.core.domain.slack;

public interface SlackClient {

    void notifyClosed(SlackWebhook webhook, String message);
}
