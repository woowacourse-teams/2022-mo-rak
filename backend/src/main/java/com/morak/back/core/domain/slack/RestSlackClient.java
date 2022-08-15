package com.morak.back.core.domain.slack;

import com.morak.back.core.exception.ExternalException;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor
@Component
public class RestSlackClient implements SlackClient {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String USERNAME = "MORAK-BOT";
    private static final String ICON_EMOJI = ":oncoming_police_car:";

    @Override
    public void notifyClosed(SlackWebhook webhook, String message) {
        NotificationRequest request = createRequest(message);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity("https://hello.com", request, String.class);
            validateOK(response);
        } catch (ResourceAccessException e) {
            throw new ExternalException("올바른 주소에대한 요청이 아닙니다. : " + webhook.getUrl());
        }
    }

    private NotificationRequest createRequest(String message) {
        return new NotificationRequest(
            USERNAME, message, ICON_EMOJI
        );
    }

    private void validateOK(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalException("슬랙 API 요청에 대한 응답이 200대가 아닙니다" + response.getStatusCode());
        }
    }
}
