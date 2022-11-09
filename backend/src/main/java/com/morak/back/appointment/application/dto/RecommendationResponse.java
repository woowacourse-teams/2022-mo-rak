package com.morak.back.appointment.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.appointment.domain.recommend.RankRecommendation;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.application.dto.MemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    private Integer rank;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime recommendStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime recommendEndDateTime;

    private List<MemberResponse> availableMembers;

    private List<MemberResponse> unavailableMembers;

    public static RecommendationResponse of(RankRecommendation recommendation, List<Member> members) {
        return new RecommendationResponse(
                recommendation.getRank(),
                recommendation.getStartDateTime(),
                recommendation.getEndDateTime(),
                recommendation.getAvailableMembers().stream()
                        .map(memberId -> MemberResponse.from(findMember(memberId, members)))
                        .collect(Collectors.toList()),
                recommendation.getUnavailableMembers().stream()
                        .map(memberId -> MemberResponse.from(findMember(memberId, members)))
                        .collect(Collectors.toList())
        );
    }

    private static Member findMember(Long memberId, List<Member> members) {
        return members.stream()
                .filter(member -> memberId.equals(member.getId()))
                .findAny()
                .orElseThrow();
    }
}
