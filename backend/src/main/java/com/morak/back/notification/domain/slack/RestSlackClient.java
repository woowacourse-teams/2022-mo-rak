package com.morak.back.notification.domain.slack;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ExternalException;
import com.morak.back.core.support.Generated;
import com.morak.back.notification.application.dto.NotificationMessageRequest;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor
@Component
@Generated
public class RestSlackClient implements SlackClient {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String USERNAME = "MORAK-BOT";
    private static final String ICON_EMOJI = ":oncoming_police_car:";

    @Override
    public void notifyMessage(SlackWebhook webhook, NotificationMessageRequest messageRequest) {
        SlackNotificationRequest slackRequest = createRequest(messageRequest);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webhook.getUrl(), slackRequest, String.class);
            validateOK(response, webhook.getUrl());
        } catch (ResourceAccessException e) {
            throw new ExternalException(CustomErrorCode.NOTIFICATION_INVALID_URL_ERROR,
                    "올바른 주소에대한 요청이 아닙니다. : " + webhook.getUrl());
        }
    }

    private SlackNotificationRequest createRequest(NotificationMessageRequest messageRequest) {
        return new SlackNotificationRequest(USERNAME, messageRequest.getMessage(), ICON_EMOJI);
    }

    private void validateOK(ResponseEntity<String> response, String url) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalException(CustomErrorCode.NOTIFICATION_REQUEST_FAILURE_ERROR,
                    String.format("슬랙 API 요청(%s)에 대한 응답이 %s 입니다.", url, response.getStatusCode()));
        }
    }
}
