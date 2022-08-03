package com.morak.back.core.domain.slack;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@RepositoryTest
class SlackWebhookRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SlackWebhookRepository webhookRepository;

    @Test
    void 웹훅을_저장한다() {
        // given
        Team team = teamRepository.save(
            Team.builder()
                .name("teamA")
                .code(Code.generate(length -> "AAAAaaaa"))
                .build()
        );
        // when
        SlackWebhook webhook = webhookRepository.save(
            SlackWebhook.builder()
                .team(team)
                .url("https://testA.com")
                .build()
        );

        // then
        assertThat(webhook.getId()).isNotNull();
    }

    @Test
    void 팀_아이디_목록으로_웹훅_목록을_가져온다() {
        // given
        Team team = teamRepository.save(
            Team.builder()
                .name("teamA?")
                .code(Code.generate(length -> "AAAAaaaa"))
                .build()
        );
        SlackWebhook webhook = webhookRepository.save(
            SlackWebhook.builder()
                .team(team)
                .url("https://testB.com")
                .build()
        );
        // when
        SlackWebhook savedWebhook = webhookRepository.findByTeamId(team.getId()).orElseThrow();
        // then
        assertThat(savedWebhook).isEqualTo(webhook);
    }
}
