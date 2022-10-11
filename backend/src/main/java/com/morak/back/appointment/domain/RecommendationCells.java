package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendationCells {

    private static final long MAX_SIZE = 10;

    private List<RecommendationCell> cells;

    public static RecommendationCells of(Appointment appointment, List<Member> members) {
        List<AppointmentTime> times = appointment.getAppointmentTimes();
        return new RecommendationCells(times.stream()
                .map(time -> RecommendationCell.of(time, members))
                .collect(Collectors.toList()));
    }

    public List<RankRecommendation> recommend(Set<AvailableTime> availableTimes) {
        List<RecommendationCell> recommendationCells = calculate(availableTimes);

        int rank = 0;
        int index = 0;
        int currentScore = Integer.MAX_VALUE;

        List<RankRecommendation> rankRecommendations = new ArrayList<>();
        for (RecommendationCell recommendationCell : recommendationCells) {
            ++index;
            int cellScore = recommendationCell.sumScore();
            if (cellScore < currentScore) {
                rank = index;
                currentScore = cellScore;
            }
            rankRecommendations.add(RankRecommendation.from(rank, recommendationCell));
        }
        return rankRecommendations;
    }

    private List<RecommendationCell> calculate(Set<AvailableTime> availableTimes) {
        for (RecommendationCell cell : cells) {
            cell.calculate(availableTimes);
        }

        return cells.stream()
                .filter(RecommendationCell::hasAnyMembers)
                .sorted()
                .limit(MAX_SIZE)
                .collect(Collectors.toList());
    }
}
