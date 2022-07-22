package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamMemberRepository;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.ResourceNotFoundException;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
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

    private static final int CODE_LENGTH = 8;

    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PollItemRepository pollItemRepository;

    public Long createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Team team = teamRepository.findByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(team.getId(), memberId);

        Poll poll = request.toPoll(member, team, PollStatus.OPEN, CodeGenerator.createRandomCode(CODE_LENGTH));
        Poll savedPoll = pollRepository.save(poll);
        List<PollItem> items = request.toPollItems(savedPoll);
        pollItemRepository.saveAll(items);

        return savedPoll.getId();
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new MismatchedTeamException("해당 그룹에 속해있지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .collect(Collectors.toList());
    }

    public void doPoll(String teamCode, Long memberId, Long pollId, List<PollItemRequest> requests) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);

        poll.doPoll(member, mapPollItemAndDescription(requests));
    }

    private Map<PollItem, String> mapPollItemAndDescription(List<PollItemRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(this::getPollItem, PollItemRequest::getDescription));
    }

    private PollItem getPollItem(PollItemRequest request) {
        return pollItemRepository.findById(request.getItemId())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);
        return PollResponse.from(poll, member);
    }

    @Transactional(readOnly = true)
    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);

        return poll.getPollItems()
                .stream()
                .map(item -> PollItemResponse.of(item, member))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollItemResultResponse> findPollItemResults(String teamCode, Long memberId, Long pollId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);

        return poll.getPollItems()
                .stream()
                .map(PollItemResultResponse::of)
                .collect(Collectors.toList());
    }

    public void deletePoll(String teamCode, Long memberId, Long id) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        poll.validateHost(member);

        pollRepository.deleteById(id);
    }

    public void closePoll(String teamCode, Long memberId, Long id) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        validateMemberInTeam(teamId, memberId);

        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        poll.close(member);
    }
}
