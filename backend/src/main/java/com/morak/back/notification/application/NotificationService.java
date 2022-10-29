package com.morak.back.notification.application;

import com.morak.back.appointment.domain.AppointmentEvent;
import com.morak.back.core.domain.MenuEvent;
import com.morak.back.notification.application.dto.NotificationMessageRequest;
import com.morak.back.notification.domain.slack.SlackClient;
import com.morak.back.notification.domain.slack.SlackWebhook;
import com.morak.back.notification.domain.slack.SlackWebhookRepository;
import com.morak.back.poll.domain.PollEvent;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Profile("master")
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final SlackWebhookRepository slackWebhookRepository;
    private final TeamRepository teamRepository;
    private final SlackClient slackClient;

    @TransactionalEventListener(condition = "#(!event.isClosed())")
    @Async
    public void notifyTeamPollOpen(PollEvent event) {
        // todo : 비동기일때 예외 발생시 로그가 찍히는지 확인
        notifyTeam(event, NotificationMessageRequest::fromPollOpen);
    }

    @TransactionalEventListener(condition = "#event.isClosed()")
    @Async
    public void notifyTeamPollClosed(PollEvent event) {
        notifyTeam(event, NotificationMessageRequest::fromPollClosed);
    }

    @TransactionalEventListener(condition = "#(!event.isClosed())")
    @Async
    public void notifyTeamAppointmentOpen(AppointmentEvent event) {
        notifyTeam(event, NotificationMessageRequest::fromAppointmentOpen);
    }

    @TransactionalEventListener(condition = "#event.isClosed()")
    @Async
    public void notifyTeamAppointmentClosed(AppointmentEvent event) {
        notifyTeam(event, NotificationMessageRequest::fromAppointmentClosed);
    }

    private void notifyTeam(MenuEvent event,
                            BiFunction<MenuEvent, Team, NotificationMessageRequest> requestBiFunction) {
        SlackWebhook webhook = slackWebhookRepository.findByTeamCode(event.getTeamCode()).orElseThrow();
        Team team = teamRepository.findByCode(event.getTeamCode()).orElseThrow();
        NotificationMessageRequest request = requestBiFunction.apply(event, team);
        slackClient.notifyMessage(webhook, request);
    }
}
