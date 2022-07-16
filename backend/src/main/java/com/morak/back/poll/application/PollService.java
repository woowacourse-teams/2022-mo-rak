package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollResult;
import com.morak.back.poll.domain.PollResultRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.ResourceNotFoundException;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final PollItemRepository pollItemRepository;

    private final PollResultRepository pollResultRepository;

    public Long createPoll(Long teamId, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Team team = teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        Poll poll = request.toPoll(member, team, PollStatus.OPEN, CodeGenerator.createRandomCode(CODE_LENGTH));
        Poll savedPoll = pollRepository.save(poll);
        List<PollItem> items = request.toPollItems(savedPoll);
        pollItemRepository.saveAll(items);

        return savedPoll.getId();
    }

    public List<PollResponse> findPolls(Long teamId, Long memberId) {
        Member member = memberRepository.getById(memberId);
        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .collect(Collectors.toList());
    }

    public void doPoll(Long tempMemberId, Long pollId, List<PollItemRequest> requests) {
        Member member = memberRepository.findById(tempMemberId).orElseThrow(ResourceNotFoundException::new);
        Poll poll = pollRepository.findById(pollId).orElseThrow(ResourceNotFoundException::new);

        Map<PollItem, PollResult> map = new HashMap<>();

        for (PollItemRequest request : requests) {
            PollItem pollItem = pollItemRepository.findById(request.getItemId()).orElseThrow(ResourceNotFoundException::new);
            map.put(pollItem, new PollResult(null, pollItem, member, request.getDescription()));
        }

//        List<Long> newItemIds = requests.stream()
//                .map(PollItemRequest::getItemId)
//                .collect(Collectors.toList());
//        List<PollItem> newItems = pollItemRepository.findAllById(newItemIds);

//        Map<PollItem, String> results = new HashMap<>();
//        for (PollItemRequest request : requests) {
//            results.put(newItems.stream()
//                    .anyMatch(item -> item.getId().equals(request.getItemId())), request.getDescription())
//        }

        poll.validateDoPoll(new ArrayList<>(map.keySet()), member);

        기존의_것_삭제(member, poll);

        pollResultRepository.saveAll(map.values());
    }

    private void 기존의_것_삭제(Member member, Poll poll) {
        for (PollItem pollItem : poll.getPollItems()) {
            pollItem.deletePollResultIfPollMember(member);
        }
    }

    public PollResponse findPoll(Long teamId, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);

        return PollResponse.from(poll, member);
    }

    public List<PollItemResponse> findPollItems(Long teamId, Long pollId) {
        // TODO: 멤버 확인해야돼!!
        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);
        return poll.getPollItems()
                .stream()
                .map(PollItemResponse::from)
                .collect(Collectors.toList());
    }

    public List<PollItemResultResponse> findPollItemResults(Long teamId, Long pollId) {
        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);
        return poll.getPollItems()
                .stream()
                .map(PollItemResultResponse::of)
                .collect(Collectors.toList());
    }

    public void deletePoll(Long teamId, Long memberId, Long id) {
        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        poll.validateHost(member);

        pollRepository.deleteById(id);
    }

    public void closePoll(Long teamId, Long memberId, Long id) {
        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        poll.close(member);
    }
}
