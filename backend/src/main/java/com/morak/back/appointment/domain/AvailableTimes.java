package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Embeddable
@NoArgsConstructor
public class AvailableTimes {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<AvailableTime> availableTimes = new HashSet<>();

    private long selectedCount = 0;

    @Transient
    private boolean first = false;

    public void select(Set<LocalDateTime> localDateTimes, Long memberId) {
        final List<AvailableTime> selectedAvailableTimes = localDateTimes.stream()
                .map(dateTime -> AvailableTime.builder().memberId(memberId).startDateTime(dateTime).build())
                .collect(Collectors.toList());
        checkFirst(memberId);
        this.availableTimes.removeIf(
                availableTime -> availableTime.matchMember(memberId) && !availableTime.isBelongTo(localDateTimes)
        );
        this.availableTimes.addAll(selectedAvailableTimes);
    }

    private void checkFirst(Long memberId) {
        if (nonExistMember(memberId)) {
            this.first = true;
        }
    }

    private boolean nonExistMember(Long memberId) {
        return this.availableTimes.stream()
                .noneMatch(availableTime -> availableTime.matchMember(memberId));
    }

    public void deleteAll() {
        this.availableTimes.clear();
    }
}
