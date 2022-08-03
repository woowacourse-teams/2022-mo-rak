package com.morak.back.core.application;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SlackClient slackClient;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private SlackWebhookRepository slackWebhookRepository;

    @InjectMocks
    private NotificationService notificationService;

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
