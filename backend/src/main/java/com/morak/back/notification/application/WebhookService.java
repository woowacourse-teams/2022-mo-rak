package com.morak.back.notification.application;

import com.morak.back.appointment.domain.AppointmentCreateEvent;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.application.AuthorizationService;
import com.morak.back.notification.domain.slack.SlackClient;
import com.morak.back.notification.domain.slack.SlackWebhook;
import com.morak.back.notification.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ExternalException;
import com.morak.back.notification.application.dto.SlackWebhookCreateRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional
public class WebhookService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final SlackClient slackClient;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;
    private final MemberRepository memberRepository;

    private final AuthorizationService authorizationService;

    public Long saveSlackWebhook(String teamCode, Long memberId, SlackWebhookCreateRequest request) {
        authorizationService.withTeamMemberValidation(() -> {
            Team team = teamRepository.findByCode(teamCode).orElseThrow(
                    () -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
            deleteOldWebhook(team);

            SlackWebhook savedWebhook = slackWebhookRepository.save(
                    SlackWebhook.builder()
                            .team(team)
                            .url(request.getUrl())
                            .build()
            );
            return savedWebhook.getId();
        }, teamCode, memberId);
    }

    private void deleteOldWebhook(Team team) {
        slackWebhookRepository.deleteByTeam(team);
        slackWebhookRepository.flush();
    }

    @TransactionalEventListener
    @Async
    public void doSomething(AppointmentCreateEvent event) {
        slackWebhookRepository.findByTeamCode(event.getTeamCode())
                .ifPresent(webhook -> slackClient.notifyMessage(webhook, message));
    }





















    // ----------------------------------------------

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
}
