package com.morak.back.brandnew.service;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.brandnew.PollCreateRequest;
import com.morak.back.brandnew.PollItemResultResponse;
import com.morak.back.brandnew.PollResponse;
import com.morak.back.brandnew.domain.NewPoll;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.repository.NewPollRepository;
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
public class NewPollService {

    private final MemberRepository memberRepository;
    private final NewPollRepository pollRepository;

//    @ValidateTeamMember
    public String createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        // TODO: 2022/10/06 teamCode, memberId로 validate
        NewPoll poll = request.toPoll(teamCode, memberId);

        return pollRepository.save(poll).getPollInfo().getCode();
    }

    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        // TODO: 2022/10/06 teamCode, memberId로 validate
        // TODO: 2022/10/06 teamCode, pollCode로 validate
        NewPoll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        return PollResponse.from(memberId, poll);
    }

    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        // TODO: 2022/10/06 teamCode, memberId로 validate
        // TODO: 2022/10/06 teamCode, pollCode로 validate
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        NewPoll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> new IllegalArgumentException("poll 없어~~"));

        poll.doPoll(member, toData(requests));
    }

    public List<PollItemResultResponse> findPollResults(String teamCode, Long memberId, String pollCode) {
        // TODO: 2022/10/06 teamCode, memberId로 validate
        // TODO: 2022/10/06 teamCode, pollCode로 validate
        NewPoll poll = pollRepository.findByCode(pollCode).orElseThrow();
        return poll.getPollItems().stream()
                .map(pollItem -> PollItemResultResponse.of(pollItem, poll.getPollInfo().getAnonymous()))
                .collect(Collectors.toList());
    }

    private Map<NewPollItem, String> toData(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(PollResultRequest::toPollItem, PollResultRequest::getDescription));
    }
}
