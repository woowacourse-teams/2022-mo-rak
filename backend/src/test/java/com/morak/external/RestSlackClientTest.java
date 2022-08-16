package com.morak.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.team.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RestSlackClientTest {

    @Test
    @Disabled // deactivate this annotation if required
    void 실제_모락_슬랙에_마감_알림을_보낸다() {
        // given
        String url = System.getenv("SLACK_ERROR_URL");
        FakeApiReceiver receiver = new FakeApiReceiver();
        SlackClient client = new FakeSlackClient(receiver);
        // when
        SlackWebhook webhook = new SlackWebhook(1L, new Team(), url);
        // then
        Assertions.assertAll(
                () -> assertThatNoException().isThrownBy(() -> client.notifyClosed(webhook, "hi")),
                () -> assertThat(receiver.getMessage()).contains("슬랙 API를 요청합니다")
        );
    }
}
