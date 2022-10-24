package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.slack.FormattableData;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollNotFoundException;
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

    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final NotificationService notificationService;

    public PollResponse createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = request.toPoll(team.getCode(), memberId);
        return PollResponse.from(memberId, pollRepository.save(poll));
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = findPollByCode(pollCode);
        validatePollInTeam(team, poll);
        return PollResponse.from(memberId, poll);
    }

    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = findPollByCode(pollCode);
        validatePollInTeam(team, poll);
        poll.doPoll(member, toDataOfSelected(requests));
    }

    private Map<PollItem, String> toDataOfSelected(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(PollResultRequest::toPollItem, PollResultRequest::getDescription));
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        return pollRepository.findAll()
                .stream()
                .map(poll -> PollResponse.from(memberId, poll))
                .sorted()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollItemResultResponse> findPollResults(String teamCode, Long memberId, String pollCode) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = findPollByCode(pollCode);
        return poll.getPollItems()
                .stream()
                .map(pollItem -> PollItemResultResponse.of(pollItem, poll.getAnonymous()))
                .collect(Collectors.toList());
    }

    public void deletePoll(String teamCode, Long memberId, String pollCode) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = findPollByCode(pollCode);
        validatePollInTeam(team, poll);
        validateHost(memberId, poll);
        pollRepository.delete(poll);
    }

    private void validateHost(Long memberId, Poll poll) {
        if (!poll.isHost(memberId)) {
            throw new PollAuthorizationException(
                    CustomErrorCode.POLL_HOST_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + poll.getCode() + " 코드 투표의 호스트가 아닙니다."
            );
        }
    }

    public void closePoll(String teamCode, Long memberId, String pollCode) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = findPollByCode(pollCode);
        poll.close(memberId);
    }

    @Transactional(readOnly = true)
    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, String pollCode) {
        Member member = findMemberById(memberId);
        Team team = findTeamByCode(teamCode);
        validateMemberInTeam(team, member);

        Poll poll = pollRepository.findFetchedByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        return poll.getPollItems()
                .stream()
                .map(pollItem -> PollItemResponse.from(pollItem, memberId))
                .collect(Collectors.toList());
    }

    @Generated
    void notifyClosedByScheduled() {
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(LocalDateTime.now());

        pollRepository.closeAll(pollsToBeClosed);
        notifyStatusAll(pollsToBeClosed);
    }

    private void notifyStatusAll(List<Poll> pollsToBeClosed) {
        Map<Team, String> teamMessages = pollsToBeClosed.stream()
                .collect(Collectors.toMap(
                        poll -> teamRepository.findByCode(poll.getTeamCode()).orElseThrow(),
                        poll -> MessageFormatter.formatClosed(FormattableData.from(poll))
                ));
        // todo : 이벤트 방식으로 전환하기.
//        notificationService.notifyAllMenuStatus(teamMessages);
    }

    private Team findTeamByCode(String teamCode) {
        return teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
    }

    private Poll findPollByCode(String pollCode) {
        return pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.ofPoll(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
    }

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(),
                    member.getId());
        }
    }

    private void validatePollInTeam(Team findTeam, Poll poll) {
        if (!poll.isBelongedTo(findTeam.getCode())) {
            throw new PollAuthorizationException(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR,
                    poll.getCode() + " 코드의 투표는 " + findTeam.getCode() + " 코드의 팀에 속해있지 않습니다."
            );
        }
    }
}
