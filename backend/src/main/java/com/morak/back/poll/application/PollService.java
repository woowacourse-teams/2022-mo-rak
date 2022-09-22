package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.slack.FormattableData;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PollService {

    private static final CodeGenerator GENERATOR = new RandomCodeGenerator();

    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PollItemRepository pollItemRepository;

    private final NotificationService notificationService;

    public String createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        Poll savedPoll = pollRepository.save(request.toPoll(member, team, Code.generate(GENERATOR)));
        notificationService.notifyMenuStatus(team, MessageFormatter.formatOpen(FormattableData.from(savedPoll)));
        return savedPoll.getCode();
    }

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(),
                    member.getId());
        }
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        List<Poll> polls = pollRepository.findAllByTeam(team);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .sorted()
                .collect(Collectors.toList());
    }

    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        Poll poll = pollRepository.findFetchedByCode(pollCode)
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

    private Map<PollItem, String> mapPollItemAndDescription2(List<PollResultRequest> requests) {
        Map<Long, String> descriptionsById = requests.stream().collect(Collectors.toMap(
                PollResultRequest::getId,
                PollResultRequest::getDescription
        ));

        List<Long> ids = requests.stream()
                .map(PollResultRequest::getId)
                .collect(Collectors.toList());
        List<PollItem> pollItems = pollItemRepository.findAllByIds(ids);

        return pollItems.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> descriptionsById.get(item.getId())
                ));
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

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
        validateMemberInTeam(team, member);

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        Poll poll = pollRepository.findFetchedByCode(pollCode)
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
        validateMemberInTeam(team, member);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateHost(member, poll);
        validateTeam(team, poll);
        pollRepository.delete(poll);
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
        validateMemberInTeam(team, member);

        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validateTeam(team, poll);

        poll.close(member);
        notificationService.notifyMenuStatus(team, MessageFormatter.formatClosed(FormattableData.from(poll)));
    }

    @Generated
    void notifyClosedByScheduled() {
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(LocalDateTime.now());

        closeAll(pollsToBeClosed);
        notifyStatusAll(pollsToBeClosed);
    }

    private void closeAll(List<Poll> pollsToBeClosed) {
        pollRepository.closeAllByIds(
                pollsToBeClosed.stream()
                        .map(Poll::getId)
                        .collect(Collectors.toList())
        );
    }

    private void notifyStatusAll(List<Poll> pollsToBeClosed) {
        Map<Team, String> teamMessages = pollsToBeClosed.stream()
                .collect(Collectors.toMap(
                        Poll::getTeam,
                        poll -> MessageFormatter.formatClosed(FormattableData.from(poll))
                ));
        notificationService.notifyAllMenuStatus(teamMessages);
    }
}
