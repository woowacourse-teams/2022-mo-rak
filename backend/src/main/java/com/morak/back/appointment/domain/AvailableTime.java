package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class AvailableTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime startDateTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @Builder
    private AvailableTime(Long id, Member member,
                          LocalDateTime startDateTime) {
        this.member = member;
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
        return Objects.equals(member, that.member) && Objects.equals(startDateTime, that.startDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, startDateTime);
    }
}
