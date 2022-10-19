package com.morak.back.core.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.ExternalException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final SlackClient slackClient;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;
    private final MemberRepository memberRepository;

    public Long saveSlackWebhook(String teamCode, Long memberId, SlackWebhookCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> new TeamNotFoundException(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

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

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(),
                    member.getId());
        }
    }

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
                .collect(Collectors.toMap(Function.identity(), webhook -> teamMessages.get(webhook.getTeam().getCode())));
    }

    private List<SlackWebhook> findAllToBeNotified(Map<String, String> teamMessages) {
        return slackWebhookRepository.findAllByTeams(teamMessages.keySet());
    }
}
