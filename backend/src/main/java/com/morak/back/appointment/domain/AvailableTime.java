package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class AvailableTime {

    private Long memberId;

    private LocalDateTime startDateTime;

    @Builder
    private AvailableTime(Long memberId, LocalDateTime startDateTime) {
        this.memberId = memberId;
        this.startDateTime = startDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        AvailableTime that = (AvailableTime) o;
        return Objects.equals(getMemberId(), that.getMemberId()) &&
                Objects.equals(getStartDateTime(), that.getStartDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getStartDateTime());
    }
}
