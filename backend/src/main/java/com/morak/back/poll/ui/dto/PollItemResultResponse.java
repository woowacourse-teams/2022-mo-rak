package com.morak.back.poll.ui.dto;

import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollResult;
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
    private List<MemberResultResponse> members;
    private String subject;

    public static PollItemResultResponse of(PollItem pollItem) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.getPollResults().size(),
                toMemberResponses(pollItem.getResultsByAnonymous()),
                pollItem.getSubject()
        );
    }

    private static List<MemberResultResponse> toMemberResponses(List<PollResult> results) {
        return results.stream()
                .map(result -> MemberResultResponse.of(result.getMember(), result.getDescription()))
                .collect(Collectors.toList());
    }
}
