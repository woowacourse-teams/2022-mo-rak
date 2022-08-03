package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class RecommendationCell implements Comparable<RecommendationCell> {

    private static final int INITIAL_SCORE = 0;

    private final DateTimePeriod dateTimePeriod;
    private final Map<Member, Integer> memberScores;

    private RecommendationCell(DateTimePeriod dateTimePeriod, Map<Member, Integer> memberScores) {
        this.dateTimePeriod = dateTimePeriod;
        this.memberScores = memberScores;
    }

    public static RecommendationCell of(LocalDateTime startDateTime, DurationMinutes durationMinutes,
                                        List<Member> members) {
        Integer minuteUnit = durationMinutes.getDurationMinutes();
        LocalDateTime endDateTime = startDateTime.plusMinutes(minuteUnit);
        DateTimePeriod dateTimePeriod = DateTimePeriod.of(startDateTime, endDateTime, minuteUnit);
        return new RecommendationCell(
                dateTimePeriod,
                members.stream()
                        .collect(Collectors.toMap(Function.identity(), member -> INITIAL_SCORE))
        );
    }

    public void calculate(List<AvailableTime> availableTimes) {
        for (AvailableTime availableTime : availableTimes) {
            increaseScoreIfAvailableTimeRange(availableTime);
        }
    }

    private void increaseScoreIfAvailableTimeRange(AvailableTime availableTime) {
        // TODO: 2022/08/03 contains 이름 바꾸기!
        if (this.dateTimePeriod.contains(availableTime.getDateTimePeriod())) {
            this.memberScores.computeIfPresent(availableTime.getMember(), (member, score) -> score + 1);
        }
    }

    public int sumScore() {
        return this.memberScores.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public boolean hasAnyMembers() {
        return sumScore() != 0;
    }

    public long getDurationUnitCount() {
        return this.dateTimePeriod.getDurationUnitCount();
    }

    @Override
    public int compareTo(RecommendationCell o) {
        return Integer.compare(o.sumScore(), this.sumScore());
    }
}
