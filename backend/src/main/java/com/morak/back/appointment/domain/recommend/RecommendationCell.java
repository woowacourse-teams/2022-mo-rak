package com.morak.back.appointment.domain.recommend;

import com.morak.back.appointment.domain.AvailableTime;
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

    private final AppointmentTime appointmentTime;
    private final Map<Long, Integer> memberIdScores;

    public static RecommendationCell of(AppointmentTime appointmentTime, List<Long> memberIds) {
        return new RecommendationCell(
                appointmentTime,
                memberIds.stream()
                        .collect(Collectors.toMap(Function.identity(), member -> INITIAL_SCORE))
        );
    }

    public void calculate(Set<AvailableTime> availableTimes) {
        for (AvailableTime availableTime : availableTimes) {
            increaseScoreIfAvailableTimeRange(availableTime);
        }
    }

    private void increaseScoreIfAvailableTimeRange(AvailableTime availableTime) {
        if (this.appointmentTime.contains(availableTime)) {
            this.memberIdScores.computeIfPresent(availableTime.getMemberId(), (member, score) -> score + 1);
        }
    }

    public int sumScore() {
        return this.memberIdScores.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public boolean hasAnyMembers() {
        return sumScore() != 0;
    }

    @Override
    public int compareTo(RecommendationCell o) {
        return Integer.compare(o.sumScore(), this.sumScore());
    }

    public Set<Long> getAvailableMembers() {
        int unitCount = appointmentTime.getUnitCount();

        return memberIdScores.entrySet().stream()
                .filter(entry -> entry.getValue() == unitCount)
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Set<Long> getUnavailableMembers() {
        Set<Long> availableMembers = getAvailableMembers();

        return memberIdScores.keySet().stream()
                .filter(member -> !availableMembers.contains(member))
                .collect(Collectors.toSet());
    }
}
