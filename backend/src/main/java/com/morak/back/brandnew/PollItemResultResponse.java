package com.morak.back.brandnew;


import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollResult;
import com.morak.back.poll.ui.dto.MemberResultResponse;
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

    public static PollItemResultResponse of(NewPollItem pollItem) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.countSelectMembers(),
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
