package com.morak.back.core.domain.slack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import javax.validation.ConstraintViolationException;
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
                .url("https://hooks.slack.com/services/test")
                .build()
        );

        // then
        assertThat(webhook.getId()).isNotNull();
    }

    @Test
    void 웹훅_URL이_잘못된_경우_예외를_던진다() {
        // given
        Team team = teamRepository.save(
                Team.builder()
                        .name("teamA")
                        .code(Code.generate(length -> "AAAAaaaa"))
                        .build()
        );

        // when
        String url = "https://not-started-with-hooks.slack.com";

        // then
        assertThatThrownBy(() -> webhookRepository.save(
                SlackWebhook.builder()
                        .team(team)
                        .url(url)
                        .build()
        )).isInstanceOf(ConstraintViolationException.class);
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
                .url("https://hooks.slack.com/services/testA")
                .build()
        );
        // when
        SlackWebhook savedWebhook = webhookRepository.findByTeamId(team.getId()).orElseThrow();
        // then
        assertThat(savedWebhook).isEqualTo(webhook);
    }

    @Test
    void 팀_아이디로_웹훅을_지운다() {
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
                .url("https://hooks.slack.com/services/testA")
                .build()
        );
        // when
        webhookRepository.deleteByTeamId(team.getId());
        // then

        assertThat(webhookRepository.findByTeamId(team.getId())).isEmpty();
    }
}
