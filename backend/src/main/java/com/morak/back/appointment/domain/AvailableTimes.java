package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvailableTimes {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_id", nullable = false, updatable = false)
    private Set<AvailableTime> availableTimes = new HashSet<>();

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
