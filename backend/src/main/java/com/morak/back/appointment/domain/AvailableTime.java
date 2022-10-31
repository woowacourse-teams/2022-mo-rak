package com.morak.back.appointment.domain;

import com.morak.back.core.domain.BaseEntity;
import com.morak.back.core.support.Generated;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "appointment_available_time")
public class AvailableTime extends BaseEntity {

    private Long memberId;

    private LocalDateTime startDateTime;

    @Builder
    private AvailableTime(Long memberId, LocalDateTime startDateTime) {
        super();
        this.memberId = memberId;
        this.startDateTime = startDateTime;
    }

    public boolean matchMember(Long memberId) {
        return this.memberId.equals(memberId);
    }

    @Override
    @Generated
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
