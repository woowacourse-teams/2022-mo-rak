package com.morak.back.notification.application;

import com.morak.back.core.application.AuthorizationService;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.notification.application.dto.SlackWebhookCreateRequest;
import com.morak.back.notification.domain.slack.SlackWebhook;
import com.morak.back.notification.domain.slack.SlackWebhookRepository;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WebhookService {

    private final TeamRepository teamRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    private final AuthorizationService authorizationService;

    public Long saveSlackWebhook(String teamCode, Long memberId, SlackWebhookCreateRequest request) {
        return authorizationService.withTeamMemberValidation(() -> {
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
}
