package com.morak.back.notification.application;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentClosedEvent;
import com.morak.back.appointment.domain.AppointmentOpenEvent;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.core.domain.MenuEvent;
import com.morak.back.core.domain.menu.Menu;
import com.morak.back.notification.application.dto.NotificationMessageRequest;
import com.morak.back.notification.domain.slack.SlackClient;
import com.morak.back.notification.domain.slack.SlackWebhook;
import com.morak.back.notification.domain.slack.SlackWebhookRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final SlackWebhookRepository slackWebhookRepository;
    private final TeamRepository teamRepository;
    private final AppointmentRepository appointmentRepository;
    private final PollRepository pollRepository;
    private final SlackClient slackClient;

    @TransactionalEventListener
    @Async
    public void notifyTeamAppointmentOpen(AppointmentOpenEvent event) {
        // todo : 비동기일때 예외 발생시 로그가 찍히는지 확인
        notifyTeam(event, NotificationMessageRequest::fromAppointmentOpen);
    }

    @TransactionalEventListener
    @Async
    public void notifyTeamAppointmentClosed(AppointmentClosedEvent event) {
        notifyTeam(event, NotificationMessageRequest::fromAppointmentClosed);
    }

    private void notifyTeam(MenuEvent event, BiFunction<Menu, Team, NotificationMessageRequest> requestBiFunction) {
        SlackWebhook webhook = slackWebhookRepository.findByTeamCode(event.getTeamCode()).orElseThrow();
        Appointment appointment = appointmentRepository.findByCode(event.getCode()).orElseThrow();
        Team team = teamRepository.findByCode(event.getTeamCode()).orElseThrow();
        NotificationMessageRequest request = requestBiFunction.apply(appointment.getMenu(), team);
        slackClient.notifyMessage(webhook, request);
    }

    /*
    @Transactional(readOnly = true)
    public void notifyMenuStatus(Team team, String message) {
        try {
            slackWebhookRepository.findByTeam(team)
                    .ifPresent(webhook -> slackClient.notifyMessage(webhook, message));
        } catch (ExternalException e) {
            logger.warn("슬랙 알림 요청 중 에러가 발생했습니다 : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public void notifyAllMenuStatus(Map<String, String> teamMessages) {
        Map<SlackWebhook, String> webHookMessages = mapWebhookToMessage(teamMessages);
        webHookMessages.forEach((webhook, message) -> notifyMenuStatus(webhook.getTeam(), message));
    }

    private Map<SlackWebhook, String> mapWebhookToMessage(Map<String, String> teamMessages) {
        List<SlackWebhook> webhooks = findAllToBeNotified(teamMessages);

        return webhooks.stream()
                .collect(Collectors.toMap(Function.identity(),
                        webhook -> teamMessages.get(webhook.getTeam().getCode())));
    }

    private List<SlackWebhook> findAllToBeNotified(Map<String, String> teamMessages) {
        return slackWebhookRepository.findAllByTeams(teamMessages.keySet());
    }
     */

}
