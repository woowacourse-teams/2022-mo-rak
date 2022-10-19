package com.morak.back.appointment.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SystemTime {

    private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    private final Clock clock;

    public SystemTime(LocalDateTime dateTime) {
        this.clock = Clock.fixed(dateTime.atOffset(zoneOffset).toInstant(), zoneOffset);
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}
