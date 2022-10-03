package com.morak.back.brandnew.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class TempDateTime {

    private LocalDateTime dateTime;

    @Transient
    private LocalDateTime now;

    @Builder
    public TempDateTime(LocalDateTime dateTime) {
        this(dateTime, LocalDateTime.now());
    }

    @Builder
    public TempDateTime(LocalDateTime dateTime, LocalDateTime now) {
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
