package com.morak.back.notification.domain.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SlackNotificationRequest {

    private String username;
    private String text;
    @JsonProperty("icon_emoji")
    private String iconEmoji;
}
