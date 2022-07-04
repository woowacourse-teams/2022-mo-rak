package com.morak.back.poll.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.ui.dto.PollCreateRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PollService {

    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final PollItemRepository pollItemRepository;

    private String tempCode = "ABCD";

    public Long createPoll(Long memberId, Long teamId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        Poll savedPoll = pollRepository.save(request.toPoll(member, team, PollStatus.OPEN, tempCode));
        List<PollItem> items = request.toPollItems(savedPoll);
        pollItemRepository.saveAll(items);

        return savedPoll.getId();
    }
}
