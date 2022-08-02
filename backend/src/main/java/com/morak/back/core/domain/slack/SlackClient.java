package com.morak.back.core.domain.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ConstructorBinding
@ConfigurationProperties(prefix = "security.notification.slack.url")
public class SlackClient {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String CHANNEL = "#slack-error";
    private static final String USERNAME = "MORAK-BOT";
    private static final String ICON_EMOJI = ":oncoming_police_car:";

    private String url;

    public SlackClient(String url) {
        this.url = url;
    }

    public void notifyException(String message) {
        NotificationRequest request = createRequest(message);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (HttpStatus.OK != response.getStatusCode()) {
            throw new RuntimeException("URL 요청에 실패했습니다.");
        }
    }

    private NotificationRequest createRequest(String message) {
        return new NotificationRequest(
                CHANNEL, USERNAME, message, ICON_EMOJI
        );
    }
}
