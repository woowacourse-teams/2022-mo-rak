package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.PollItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PollItemResponse {

    private final Long id;

    private final String subject;

    @JsonProperty("isSelected")
    private final Boolean selected;

    private final String description;

    public static PollItemResponse of(PollItem pollItem, Member member) {
        return new PollItemResponse(
                pollItem.getId(),
                pollItem.getSubject(),
                pollItem.isSelectedBy(member),
                pollItem.getDescriptionFrom(member)
        );
    }
}
