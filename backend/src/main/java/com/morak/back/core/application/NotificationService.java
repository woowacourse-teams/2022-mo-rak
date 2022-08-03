package com.morak.back.core.application;

import com.morak.back.auth.exception.TeamNotFoundException;
import com.morak.back.core.domain.Menu;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.MismatchedTeamException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SlackClient slackClient;
    private final PollRepository pollRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void notifyPoll() {
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(LocalDateTime.MIN, LocalDateTime.now());
        Map<Menu, SlackWebhook> pollWebhooks = joinPollsWithWebhooks(pollsToBeClosed);
        for (Entry<Menu, SlackWebhook> entry : pollWebhooks.entrySet()) {
            Menu menu = entry.getKey();
            menu.close(menu.getHost());
            slackClient.notifyClosed(entry.getValue(), MessageFormatter.format(menu));
        }
    }

    private Map<Menu, SlackWebhook> joinPollsWithWebhooks(List<Poll> pollsToBeClosed) {
        return pollsToBeClosed.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                poll -> slackWebhookRepository.findByTeamId(poll.getTeam().getId()).orElseThrow()
            ));
    }

    public Long saveSlackWebhook(String teamCode, Long memberId, SlackWebhookCreateRequest request) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
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
            throw new MismatchedTeamException(teamId, memberId);
        }
    }
}
