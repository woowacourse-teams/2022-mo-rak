package com.morak.back.poll.ui.dto;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.poll.domain.PollItem;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollItemResultResponse {
    private Long id;
    private Integer count;
    private List<MemberResponse> members;
    private String subject;

    public static PollItemResultResponse of(PollItem pollItem) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.getPollResults().size(),
                toMemberResponses(pollItem.getMembersByAnonymous()),
                pollItem.getSubject()
        );
    }

    private static List<MemberResponse> toMemberResponses(List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
}
