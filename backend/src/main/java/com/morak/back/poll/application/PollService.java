package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.auth.exception.TeamNotFoundException;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.PollItemNotFoundException;
import com.morak.back.poll.exception.PollNotFoundException;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.MismatchedTeamException;
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

    public Long createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = request.toPoll(member, team, PollStatus.OPEN, GENERATOR.generate(8));
        Poll savedPoll = pollRepository.save(poll);
        List<PollItem> items = request.toPollItems(savedPoll);
        pollItemRepository.saveAll(items);

        return savedPoll.getId();
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new MismatchedTeamException(teamId, memberId);
        }
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .collect(Collectors.toList());
    }

    public void doPoll(String teamCode, Long memberId, Long pollId, List<PollResultRequest> requests) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        poll.doPoll(member, mapPollItemAndDescription(requests));
    }

    private Map<PollItem, String> mapPollItemAndDescription(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(this::getPollItem, PollResultRequest::getDescription));
    }

    private PollItem getPollItem(PollResultRequest request) {
        Long pollItemId = request.getItemId();
        return pollItemRepository.findById(pollItemId).orElseThrow(() -> new PollItemNotFoundException(pollItemId));
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId));
        return PollResponse.from(poll, member);
    }

    @Transactional(readOnly = true)
    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        return poll.getPollItems()
                .stream()
                .map(item -> PollItemResponse.of(item, member))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollItemResultResponse> findPollItemResults(String teamCode, Long memberId, Long pollId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        return poll.getPollItems()
                .stream()
                .map(PollItemResultResponse::of)
                .collect(Collectors.toList());
    }

    public void deletePoll(String teamCode, Long memberId, Long pollId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId, teamId));
        poll.validateHost(member);

        pollRepository.deleteById(pollId);
    }

    public void closePoll(String teamCode, Long memberId, Long pollId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId)
                .orElseThrow(() -> new PollNotFoundException(pollId, teamId));
        poll.close(member);
    }
}
