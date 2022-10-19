package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@Builder
public class SystemDateTime {

    @Column(name = "closed_at")
    private LocalDateTime dateTime;

    @Transient
    private LocalDateTime now;

    public SystemDateTime(LocalDateTime dateTime) {
        this(dateTime, LocalDateTime.now());
    }

    public SystemDateTime(LocalDateTime dateTime, LocalDateTime now) {
        this.dateTime = dateTime;
        this.now = now;
    }

    public boolean beforeNow() {
        return dateTime.isBefore(now);
    }

    public boolean afterNow() {
        return dateTime.isAfter(now);
    }
}
