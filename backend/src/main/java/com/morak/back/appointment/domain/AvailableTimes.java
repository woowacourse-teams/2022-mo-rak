package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
                .anyMatch(availableTime -> availableTime.getMemberId().equals(memberId));
    }

    public void select(Set<LocalDateTime> localDateTimes, Long memberId) {
        Set<AvailableTime> availableTimes = new HashSet<>();
        for (LocalDateTime dateTime : localDateTimes) {
            availableTimes.add(AvailableTime.builder().memberId(memberId).startDateTime(dateTime).build());
        }
        this.availableTimes.removeIf(availableTime -> availableTime.getMemberId().equals(memberId));
        this.availableTimes.addAll(availableTimes);
    }
}
