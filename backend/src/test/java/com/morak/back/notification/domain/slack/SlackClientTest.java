package com.morak.back.notification.domain.slack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.notification.application.dto.NotificationMessageRequest;
import com.morak.back.team.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SlackClientTest {

    @Test
    void 모락_슬랙에_마감_알림을_보낸다() {
        // given
        FakeApiReceiver receiver = new FakeApiReceiver();
        SlackClient client = new FakeSlackClient(receiver);
        // when
        SlackWebhook webhook = new SlackWebhook(1L, new Team(), "https://hooks.slack.com/services/testing");
        // then
        Assertions.assertAll(
                () -> assertThatNoException().isThrownBy(
                        () -> client.notifyMessage(webhook, new NotificationMessageRequest("hi"))
                ),
                () -> assertThat(receiver.getMessage()).contains("hi")
        );
    }
}
