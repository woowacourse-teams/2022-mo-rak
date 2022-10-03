package com.morak.back.brandnew.service;

import com.morak.back.brandnew.PollCreateRequest;
import com.morak.back.brandnew.PollResponse;
import com.morak.back.brandnew.domain.Member;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.domain.PollManager;
import com.morak.back.brandnew.repository.NewMemberRepository;
import com.morak.back.brandnew.repository.PollManagerRepository;
import com.morak.back.poll.ui.dto.PollResultRequest;
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

    private final NewMemberRepository memberRepository;
    private final PollManagerRepository pollManagerRepository;

    public void createPoll(Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        PollManager pollManager = request.toPollManager(member);

        pollManagerRepository.save(pollManager);
    }

    public PollResponse findPoll(Long memberId, String pollCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        PollManager pollManager = pollManagerRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        return PollResponse.from(member, pollManager);
    }

    public void selectPollItems(Long memberId, String pollCode, List<PollResultRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        PollManager pollManager = pollManagerRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        pollManager.select(member, toData(requests));
    }

    private Map<NewPollItem, String> toData(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(PollResultRequest::toPollItem, PollResultRequest::getDescription));
    }
}
