package com.morak.back.poll.ui.dto;

import java.time.LocalDateTime;

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
    private final String closedAt;
    private final String code;
    private final Boolean isHost;
}
