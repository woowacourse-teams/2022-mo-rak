package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
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
    private final Map<Member, Integer> memberScores;

    public static RecommendationCell of(AppointmentTime appointmentTime, List<Member> members) {
        return new RecommendationCell(
                appointmentTime,
                members.stream()
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

    @Override
    public int compareTo(RecommendationCell o) {
        return Integer.compare(o.sumScore(), this.sumScore());
    }

    public Set<Member> getAvailableMembers() {
        int unitCount = appointmentTime.getUnitCount();

        return memberScores.entrySet().stream()
                .filter(entry -> entry.getValue() == unitCount)
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Set<Member> getUnavailableMembers() {
        Set<Member> availableMembers = getAvailableMembers();

        return memberScores.keySet().stream()
                .filter(member -> !availableMembers.contains(member))
                .collect(Collectors.toSet());
    }
}
