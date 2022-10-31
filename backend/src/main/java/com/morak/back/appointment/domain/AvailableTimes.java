package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class AvailableTimes {

    @ElementCollection
    @CollectionTable(
            name = "appointment_available_time",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    private final Set<AvailableTime> availableTimes = new HashSet<>();

    public boolean hasMember(Long memberId) {
        return this.availableTimes.stream()
                .anyMatch(availableTime -> availableTime.matchMember(memberId));
    }

    public void select(Set<LocalDateTime> localDateTimes, Long memberId) {
        final List<AvailableTime> availableTimes = localDateTimes.stream()
                .map(dateTime -> AvailableTime.builder().memberId(memberId).startDateTime(dateTime).build())
                .collect(Collectors.toList());

        this.availableTimes.removeIf(availableTime -> availableTime.getMemberId().equals(memberId));
        this.availableTimes.addAll(availableTimes);
    }
}
