package com.morak.back.notification.domain.slack;

import com.morak.back.notification.application.dto.NotificationMessageRequest;

public interface SlackClient {

    void notifyMessage(SlackWebhook webhook, NotificationMessageRequest messageRequest);
}
