package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.Menu;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollNotFoundException;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PollService {

    private static final CodeGenerator GENERATOR = new RandomCodeGenerator();
    private static final String MENU_NAME = "투표";

    private final SlackClient slackClient;
    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PollItemRepository pollItemRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    private final NotificationService notificationService;

    public String createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = request.toPoll(member, team, PollStatus.OPEN, Code.generate(GENERATOR));
        List<PollItem> items = request.toPollItems(poll);

        for (PollItem item : items) {
            poll.addItem(item);
        }

        Poll savedPoll = pollRepository.save(poll);

        return savedPoll.getCode();
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, teamId, memberId);
        }
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Long teamId = teamRepository.findIdByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(teamId, memberId);

        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .sorted()
                .collect(Collectors.toList());
    }

    // TODO: 2022/08/11 PollResultResponse에 같은 itemId가 들어오는 경우 ?
    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);
        poll.doPoll(member, mapPollItemAndDescription(requests));
    }

    private void validateTeam(Team findTeam, Poll poll) {
        if (!poll.isBelongedTo(findTeam)) {
            throw new PollAuthorizationException(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR,
                    poll.getCode() + " 코드의 투표는 " + findTeam.getCode() + " 코드의 팀에 속해있지 않습니다."
            );
        }
    }

    private Map<PollItem, String> mapPollItemAndDescription(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(this::getPollItem, PollResultRequest::getDescription));
    }

    private PollItem getPollItem(PollResultRequest request) {
        Long pollItemId = request.getId();
        return pollItemRepository.findById(pollItemId)
                .orElseThrow(
                        () -> PollNotFoundException.ofPollItem(CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR, pollItemId));
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);
        return PollResponse.from(poll, member);
    }

    @Transactional(readOnly = true)
    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);

        return poll.getPollItems()
                .stream()
                .map(item -> PollItemResponse.of(item, member))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollItemResultResponse> findPollItemResults(String teamCode, Long memberId, String pollCode) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);

        return poll.getPollItems()
                .stream()
                .map(PollItemResultResponse::of)
                .collect(Collectors.toList());
    }

    public void deletePoll(String teamCode, Long memberId, String pollCode) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateHost(member, poll);
        validateTeam(team, poll);
        pollRepository.deleteById(poll.getId());
    }

    private void validateHost(Member member, Poll poll) {
        if (!poll.isHost(member)) {
            throw new PollAuthorizationException(
                    CustomErrorCode.POLL_HOST_MISMATCHED_ERROR,
                    member.getId() + "번 멤버는 " + poll.getCode() + " 코드 투표의 호스트가 아닙니다."
            );
        }
    }

    public void closePoll(String teamCode, Long memberId, String pollCode) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);

        poll.close(member);
        notificationService.notifyMenuStatus(team, poll, MessageFormatter::formatClosed);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Generated
    void notifyClosedByScheduled() {
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(LocalDateTime.MIN, LocalDateTime.now());

        Map<Menu, Optional<SlackWebhook>> pollWebhooks = joinPollsWithWebhooks(pollsToBeClosed);
        notificationService.closeAndNotifyMenusByScheduled(pollWebhooks);
    }

    private Map<Menu, Optional<SlackWebhook>> joinPollsWithWebhooks(List<Poll> pollsToBeClosed) {
        return pollsToBeClosed.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        poll -> slackWebhookRepository.findByTeamId(poll.getTeam().getId())
                ));
    }
}
