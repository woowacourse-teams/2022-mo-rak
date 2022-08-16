package com.morak.back.core.application;

import com.morak.back.core.domain.Menu;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ExternalException;
import com.morak.back.core.exception.SchedulingException;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

        slackWebhookRepository.deleteByTeamId(team.getId());
        slackWebhookRepository.flush();
        SlackWebhook savedWebhook = slackWebhookRepository.save(
                SlackWebhook.builder()
                        .team(team)
                        .url(request.getUrl())
                        .build()
        );
        return savedWebhook.getId();
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, teamId, memberId);
        }
    }

    public void notifyMenuStatus(Team team, Menu menu, BiFunction<Menu, Team, String> formatter) {
        slackWebhookRepository.findByTeamId(team.getId())
                .ifPresent(webhook -> slackClient.notifyClosed(
                        webhook, formatter.apply(menu, team)
                ));
    }

    public void closeAndNotifyMenusByScheduled(Map<Menu, Optional<SlackWebhook>> menuWebhooks) {
        List<ExternalException> exceptions = menuWebhooks.entrySet()
                .stream()
                .map(entry -> closeAndNotify(entry.getKey(), entry.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        throwsIfAnyWebhookFailed(exceptions);
    }

    private Optional<ExternalException> closeAndNotify(Menu menu, Optional<SlackWebhook> optionalWebhook) {
        try {
            optionalWebhook.ifPresent(webhook ->
                    slackClient.notifyClosed(webhook, MessageFormatter.formatClosed(menu, webhook.getTeam())));
            menu.close(menu.getHost());
            return Optional.empty();
        } catch (ExternalException e) {
            return Optional.of(e);
        }
    }

    private void throwsIfAnyWebhookFailed(List<ExternalException> exceptions) {
        if (exceptions.isEmpty()) {
            return;
        }
        throw new SchedulingException(
                CustomErrorCode.NOTIFICATION_SCHEDULING_FAILURE_ERROR,
                "스케줄링 실행 중 실패 케이스가 존재합니다.",
                exceptions
        );
    }
}
