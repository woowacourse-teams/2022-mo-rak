package com.morak.back.poll.ui.dto;

import com.morak.back.poll.domain.PollItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PollItemResponse {

    private final Long id;
    private final String subject;

    public static PollItemResponse from(PollItem pollItem) {
        return new PollItemResponse(pollItem.getId(), pollItem.getSubject());
    }
}
