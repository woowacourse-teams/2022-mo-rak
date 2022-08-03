package com.morak.back.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doNothing;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SlackClient slackClient;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private SlackWebhookRepository slackWebhookRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void 웹훅_URL을_등록한다() {
        // given
        Team team = Team.builder()
            .id(1L)
            .build();
        String url = "https://hello.world";

        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        doNothing().when(slackWebhookRepository).deleteByTeamId(anyLong());
        given(slackWebhookRepository.save(any(SlackWebhook.class)))
            .willReturn(
                SlackWebhook.builder()
                    .id(10L)
                    .team(team)
                    .url(url)
                    .build()
            );

        SlackWebhookCreateRequest request = new SlackWebhookCreateRequest(url);
        // when
        Long webhookId = notificationService.saveSlackWebhook("teamCODE", request);

        // then
        assertThat(webhookId).isEqualTo(10L);
    }

    @Test
    void 상태가_OPEN이고_마감시간이_지난_투표를_마감하고_알림을_보낸다() {
        // given
        Member host = new Member();
        given(pollRepository.findAllToBeClosed(any(), any()))
            .willReturn(List.of(
                    Poll.builder()
                        .host(host)
                        .status(PollStatus.OPEN)
                        .team(
                            Team.builder()
                                .id(1L)
                                .build()
                        ).build(),
                    Poll.builder()
                        .host(host)
                        .status(PollStatus.OPEN)
                        .team(
                            Team.builder()
                                .id(2L)
                                .build()
                        ).build()
                )
            );
        given(slackWebhookRepository.findByTeamId(anyLong())).willReturn(Optional.of(new SlackWebhook()));
        doNothing().when(slackClient).notifyClosed(any(SlackWebhook.class), anyString());
        // when
        notificationService.notifyPoll();
        // then
        verify(pollRepository).findAllToBeClosed(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(slackWebhookRepository, times(2)).findByTeamId(anyLong());
        verify(slackClient, times(2)).notifyClosed(any(SlackWebhook.class), anyString());
    }

}
