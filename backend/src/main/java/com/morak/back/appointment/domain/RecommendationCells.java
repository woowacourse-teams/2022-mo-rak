package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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

    public static RecommendationCells of(Appointment appointment, List<Member> members) {
        LocalDateTime firstStartDateTime = appointment.getStartDateTime();
        LocalDateTime lastEndDateTime = appointment.getEndDateTime();
        DurationMinutes durationMinutes = appointment.getDurationMinutes();

        return new RecommendationCells(Stream.iterate(
                        firstStartDateTime,
                        isStartTimeWithDurationNotOverEndTime(durationMinutes, lastEndDateTime),
                        s -> s.plusMinutes(Appointment.MINUTES_UNIT))
                .map(s -> RecommendationCell.of(s, durationMinutes, members))
                .collect(Collectors.toList()));
    }

    private static Predicate<LocalDateTime> isStartTimeWithDurationNotOverEndTime(DurationMinutes durationMinutes,
                                                                                  LocalDateTime endDateTime) {
        return startDateTime -> !startDateTime.plusMinutes(durationMinutes.getDurationMinutes()).isAfter(endDateTime);
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
            rankRecommendations.add(RankRecommendation.from(rank, recommendationCell));
        }
        return rankRecommendations;
    }

    private List<RecommendationCell> calculate(List<AvailableTime> availableTimes) {
        // TODO: 2022/08/03 이름 바꾸기! 
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
