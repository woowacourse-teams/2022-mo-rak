package com.morak.back.appointment.domain.recommand;

import com.morak.back.auth.domain.Member;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankRecommendation {

    private final int rank;
    private final RecommendationDateTimePeriod dateTimePeriod;
    private final List<Member> availableMembers;
    private final List<Member> unavailableMembers;

    public static RankRecommendation from(int rank, RecommendationCell recommendationCell, int minuteUnit) {
        int durationUnitCount = recommendationCell.getDurationUnitCount(minuteUnit);
        List<Member> availableMembers = recommendationCell.getAvailableMembers(durationUnitCount);
        List<Member> unavailableMembers = recommendationCell.getUnavailableMembers(availableMembers);

        return new RankRecommendation(
                rank,
                recommendationCell.getRecommendationDateTimePeriod(),
                availableMembers,
                unavailableMembers
        );
    }
}
