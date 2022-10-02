package com.morak.back.brandnew.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class FakeDateTime implements MorakDateTime {

    private LocalDateTime dateTime;

    @Transient
    private LocalDateTime now;

    @Builder
    public FakeDateTime(LocalDateTime dateTime, LocalDateTime now) {
        this.dateTime = dateTime;
        this.now = now;
    }

    @Override
    public boolean beforeNow() {
        return dateTime.isBefore(now);
    }

    @Override
    public boolean afterNow() {
        return dateTime.isAfter(now);
    }
}
