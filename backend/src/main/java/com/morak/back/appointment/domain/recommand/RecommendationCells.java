package com.morak.back.appointment.domain.recommand;

import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.appointment.domain.availabletime.AvailableTime;
import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendationCells {

    private static final long MAX_SIZE = 10;

    private List<RecommendationCell> cells;
    private int minuteUnit;

    public static RecommendationCells of(Appointment appointment, List<Member> members) {

        LocalDateTime firstStartDateTime = appointment.getStartDateTime();
        LocalDateTime lastEndDateTime = appointment.getEndDateTime();
        int minuteUnit = appointment.getMinuteUnit();

        RecommendationDateTimePeriod recommendationLimitDateTimePeriod =
                new RecommendationDateTimePeriod(firstStartDateTime, lastEndDateTime);

        int durationMinute = appointment.getDurationTime();

        return new RecommendationCells(Stream.iterate(
                        new RecommendationDateTimePeriod(firstStartDateTime, firstStartDateTime.plusMinutes(durationMinute)),
                        recommendationLimitDateTimePeriod::contains,
                        recommendationDateTimePeriod -> recommendationDateTimePeriod.getNextPeriod(minuteUnit))
                .map(dateTimePeriod -> RecommendationCell.of(dateTimePeriod, members))
                .collect(Collectors.toList()), minuteUnit);
    }

    public List<RankRecommendation> recommend(List<AvailableTime> availableTimes) {
        List<RecommendationCell> recommendationCells = calculate(availableTimes);
        int rank = 0;
        int index = 0;
        int currentScore = Integer.MAX_VALUE;

        List<RankRecommendation> rankRecommendations = new ArrayList<>();
        for (RecommendationCell recommendationCell : recommendationCells) {
            int cellScore = recommendationCell.sumScore();
            ++index;
            if (cellScore < currentScore) {
                rank = index;
                currentScore = cellScore;
            }
            rankRecommendations.add(RankRecommendation.from(rank, recommendationCell, minuteUnit));
        }
        return rankRecommendations;
    }

    private List<RecommendationCell> calculate(List<AvailableTime> availableTimes) {
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
