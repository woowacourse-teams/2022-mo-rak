package com.morak.back.core.domain.slack;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
            ResponseEntity<String> response = restTemplate.postForEntity(webhook.getUrl(), request, String.class);
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
//        if (HttpStatus.OK != response.getStatusCode()) {
//            throw new ExternalException("슬랙 메세지를 보내는데 실패했습니다. status_code = " + response.getStatusCode());
//        }
    }

    public NotificationRequest createRequest(String message) {
        return new NotificationRequest(
            USERNAME, message, ICON_EMOJI
        );
    }
}
