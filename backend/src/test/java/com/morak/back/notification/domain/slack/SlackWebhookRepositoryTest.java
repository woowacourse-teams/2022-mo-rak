package com.morak.back.notification.domain.slack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    void 팀으로_웹훅_목록을_가져온다() {
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
        SlackWebhook savedWebhook = webhookRepository.findByTeam(team).orElseThrow();
        // then
        assertThat(savedWebhook).isEqualTo(webhook);
    }

    @Test
    void 팀_목록으로_웹훅_목록을_조회한다() {
        // given
        List<Team> teams = new ArrayList<>();
        int iterationCount = 10;

        for (int i = 0; i < iterationCount; i++) {
            String code = "AAAAaaa" + i;
            Team savedTeam = teamRepository.save(
                    Team.builder()
                            .name("team" + i)
                            .code(Code.generate(length -> code))
                            .build()
            );
            webhookRepository.save(
                    SlackWebhook.builder()
                            .team(savedTeam)
                            .url("https://hooks.slack.com/services/testA")
                            .build()
            );
            teams.add(savedTeam);
        }

        // when
        List<SlackWebhook> webhooks = webhookRepository.findAllByTeams(
                teams.stream()
                        .map(Team::getCode)
                        .collect(Collectors.toList())
        );

        // then
        assertThat(webhooks).hasSize(iterationCount);
    }

    @Test
    void 팀으로_웹훅을_지운다() {
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
        webhookRepository.deleteByTeam(team);
        // then

        assertThat(webhookRepository.findByTeam(team)).isEmpty();
    }
}
