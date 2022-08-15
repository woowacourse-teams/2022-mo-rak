package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RankRecommendation {

    // TODO: 2022/08/03  dateTimePeriod, availableMembers, unavailableMembers 묶기?!
    private final int rank;
    private final DateTimePeriod dateTimePeriod;
    private final List<Member> availableMembers;
    private final List<Member> unavailableMembers;

    public static RankRecommendation from(int rank, RecommendationCell recommendationCell) {
        Map<Member, Integer> memberScores = recommendationCell.getMemberScores();
        long durationUnitCount = recommendationCell.getDurationUnitCount();
        List<Member> availableMembers = getAvailableMembers(memberScores, durationUnitCount);
        List<Member> unavailableMembers = getUnavailableMembers(memberScores, availableMembers);

        return new RankRecommendation(
                rank,
                recommendationCell.getDateTimePeriod(),
                availableMembers,
                unavailableMembers
        );
    }

    private static List<Member> getAvailableMembers(Map<Member, Integer> memberScores, long durationUnitCount) {
        return memberScores.entrySet().stream()
                .filter(entry -> entry.getValue() == durationUnitCount)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    private static List<Member> getUnavailableMembers(Map<Member, Integer> memberScores,
                                                      List<Member> availableMembers) {
        Set<Member> members = memberScores.keySet();
        availableMembers.forEach(members::remove);
        return new ArrayList<>(members);
    }
}
