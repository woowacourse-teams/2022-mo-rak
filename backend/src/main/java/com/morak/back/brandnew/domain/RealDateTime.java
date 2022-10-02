package com.morak.back.brandnew.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RealDateTime implements MorakDateTime {

    private final LocalDateTime dateTime;

    protected RealDateTime() {
        this(null);
    }

    public RealDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean beforeNow() {
        return dateTime.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean afterNow() {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
