package com.morak.back.core.domain.slack;

public interface SlackClient {

    void notifyMenuStatus(SlackWebhook webhook, String message);
}
