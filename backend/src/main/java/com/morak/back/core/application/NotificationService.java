package com.morak.back.core.application;

import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final SlackClient slackClient;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    public Long saveSlackWebhook(String teamCode, Long memberId, SlackWebhookCreateRequest request) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> new TeamNotFoundException(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        deleteOldWebhook(team);
        SlackWebhook savedWebhook = slackWebhookRepository.save(
                SlackWebhook.builder()
                        .team(team)
                        .url(request.getUrl())
                        .build()
        );
        return savedWebhook.getId();
    }

    private void deleteOldWebhook(Team team) {
        slackWebhookRepository.deleteByTeam(team);
        slackWebhookRepository.flush();
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, teamId, memberId);
        }
    }

    @Transactional(readOnly = true)
    public void notifyMenuStatus(Team team, String message) {
        slackWebhookRepository.findByTeam(team)
                .ifPresent(webhook -> slackClient.notifyMessage(webhook, message));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void notifyAllMenuStatus(Map<Team, String> teamMessages) {
        Map<SlackWebhook, String> webHookMessages = mapWebhookToMessage(teamMessages);
        webHookMessages.forEach(slackClient::notifyMessage);
    }

    private Map<SlackWebhook, String> mapWebhookToMessage(Map<Team, String> teamMessages) {
        List<SlackWebhook> webhooks = findAllToBeNotified(teamMessages);

        return webhooks.stream()
                .collect(Collectors.toMap(Function.identity(), webhook -> teamMessages.get(webhook.getTeam())));
    }

    private List<SlackWebhook> findAllToBeNotified(Map<Team, String> teamMessages) {
        return slackWebhookRepository.findAllByTeams(teamMessages.keySet());
    }
}
