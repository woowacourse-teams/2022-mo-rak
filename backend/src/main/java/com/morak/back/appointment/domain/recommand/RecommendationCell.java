package com.morak.back.appointment.domain.recommand;

import com.morak.back.appointment.domain.availabletime.AvailableTime;
import com.morak.back.auth.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendationCell implements Comparable<RecommendationCell> {

    private static final int INITIAL_SCORE = 0;

    private final RecommendationDateTimePeriod recommendationDateTimePeriod;
    private final Map<Member, Integer> memberScores;

    public static RecommendationCell of(RecommendationDateTimePeriod recommendationDateTimePeriod,
                                        List<Member> members) {
        return new RecommendationCell(
                recommendationDateTimePeriod,
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
        if (this.recommendationDateTimePeriod.contains(availableTime.getAvailableTimeDateTimePeriod())) {
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

    public int getDurationUnitCount(int minuteUnit) {
        return (int) this.recommendationDateTimePeriod.countDurationUnit(minuteUnit);
    }

    public List<Member> getAvailableMembers(int score) {
        return memberScores.entrySet().stream()
                .filter(entry -> entry.getValue() == score)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Member> getUnavailableMembers(List<Member> availableMembers) {
        Set<Member> members = memberScores.keySet();
        availableMembers.forEach(members::remove);
        return new ArrayList<>(members);
    }

    @Override
    public int compareTo(RecommendationCell o) {
        return Integer.compare(o.sumScore(), this.sumScore());
    }
}
