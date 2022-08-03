package com.morak.back.core.domain.slack;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.team.domain.Team;
import org.junit.jupiter.api.Test;

class SlackClientTest {

    @Test
    void 모락_슬랙에_마감_알림을_보낸다() {
        // given
        SlackClient client = new FakeSlackClient();
        // when
        SlackWebhook webhook = new SlackWebhook(1L, new Team(),
            "https://hooks.slack.com/services/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");
        // then
        assertThatNoException().isThrownBy(
            () -> client.notifyClosed(webhook, "hi")
        );
    }
}
