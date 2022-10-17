package com.morak.back.brandnew.service;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.brandnew.domain.NewPoll;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.repository.NewPollRepository;
import com.morak.back.core.exception.CustomErrorCode;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class NewPollService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final NewPollRepository pollRepository;

    //    @ValidateTeamMember
    public String createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = request.toPoll(team.getId(), memberId);

        return pollRepository.save(poll).getPollInfo().getCode();
    }

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(), member.getId());
        }
    }

    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        return PollResponse.from(memberId, poll);
    }

    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        poll.doPoll(member, toData(requests));
    }

    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        return pollRepository.findAll().stream()
                .map(poll -> PollResponse.from(memberId, poll))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<PollItemResultResponse> findPollResults(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = pollRepository.findByCode(pollCode).orElseThrow();
        return poll.getPollItems().stream()
                .map(pollItem -> PollItemResultResponse.of(pollItem, poll.getPollInfo().getAnonymous()))
                .collect(Collectors.toList());
    }

    private Map<NewPollItem, String> toData(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(PollResultRequest::toPollItem, PollResultRequest::getDescription));
    }

    public void deletePoll(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = pollRepository.findByCode(pollCode).orElseThrow();
        pollRepository.delete(poll);
    }

    public void closePoll(String teamCode, Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);
        NewPoll poll = pollRepository.findByCode(pollCode).orElseThrow();
        poll.close(memberId);
    }

    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, String pollCode) {
        NewPoll poll = pollRepository.findFetchedByCode(pollCode).orElseThrow();
        return poll.getPollItems().stream()
                .map(pollItem -> PollItemResponse.from(pollItem, memberId))
                .collect(Collectors.toList());
    }
}
