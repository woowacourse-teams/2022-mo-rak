package com.morak.back.poll.ui.dto;

import com.morak.back.auth.ui.dto.MemberResponse;
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
    private List<MemberResponse> members;
    private String subject;

    public static PollItemResultResponse of(PollItem pollItem) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.getPollResults().size(),
                toMemberResponses(pollItem.getResultsByAnonymous()),
                pollItem.getSubject()
        );
    }

    private static List<MemberResponse> toMemberResponses(List<PollResult> results) {
        return results.stream()
                .map(result -> MemberResponse.of(result.getMember(), result.getDescription()))
                .collect(Collectors.toList());
    }

    // TODO: 2022/07/16 현재는 쓰지 않습니다.
//    private static List<MemberResponse> toMemberResponses(List<Member> members) {
//        return members.stream()
//                .map(MemberResponse::of)
//                .collect(Collectors.toList());
//    }
}
