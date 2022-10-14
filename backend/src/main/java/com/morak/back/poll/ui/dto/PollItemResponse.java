package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.auth.domain.Member;
import com.morak.back.brandnew.domain.NewPollItem;
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

    public static PollItemResponse of(PollItem pollItem, Member member) {
        return new PollItemResponse(
                pollItem.getId(),
                pollItem.getSubject(),
                pollItem.isSelectedBy(member),
                pollItem.getDescriptionFrom(member)
        );
    }

    public static PollItemResponse from(NewPollItem pollItem, Long memberId) {
        return new PollItemResponse(
                pollItem.getId(),
                pollItem.getSubject(),
                pollItem.isSelectedBy(memberId),
                pollItem.getDescriptionFrom(memberId)
        );
    }
}
