package com.morak.back.poll.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.poll.domain.PollItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollItemResponse {

    private Long id;

    private String subject;

    @JsonProperty("isSelected")
    private Boolean selected;

    private String description;

    public static PollItemResponse from(PollItem pollItem, Long memberId) {
        return new PollItemResponse(
                pollItem.getId(),
                pollItem.getSubject(),
                pollItem.isSelectedBy(memberId),
                pollItem.getDescriptionFrom(memberId)
        );
    }
}
