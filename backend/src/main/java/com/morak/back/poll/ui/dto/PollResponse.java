package com.morak.back.poll.ui.dto;

import java.time.LocalDateTime;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PollResponse {

    private final Long id;
    private final String title;
    private final Integer allowedPollCount;
    private final Boolean isAnonymous;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime closedAt;
    private final String code;
    private final Boolean isHost;

    public static PollResponse from(Poll poll, Member member) {
        return new PollResponse(
            poll.getId(),
            poll.getTitle(),
            poll.getAllowedPollCount(),
            poll.getIsAnonymous(),
            poll.getStatus().name(),
            poll.getCreatedAt(),
            poll.getClosedAt(),
            poll.getCode(),
            poll.isHost(member)
        );
    }
}
