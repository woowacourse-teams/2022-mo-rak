package com.morak.back.notification.domain.slack;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ExternalException;
import com.morak.back.notification.application.dto.NotificationMessageRequest;
import com.morak.back.support.FakeBean;

@FakeBean
public class FakeSlackClient implements SlackClient {

    private FakeApiReceiver receiver;

    public FakeSlackClient(FakeApiReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void notifyMessage(SlackWebhook webhook, NotificationMessageRequest messageRequest) {
        String message = messageRequest.getMessage();
        if (message.contains("invalid")) {
            throw new ExternalException(
                    CustomErrorCode.NOTIFICATION_REQUEST_FAILURE_ERROR,
                    "요청 실패 ㅠ"
            );
        }

        receiver.setMessage("슬랙 API를 요청합니다\n" +
                "url = " + webhook.getUrl() + "\n" +
                "message = " + message);
    }
}
