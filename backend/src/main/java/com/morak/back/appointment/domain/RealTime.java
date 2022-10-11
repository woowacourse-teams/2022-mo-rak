package com.morak.back.appointment.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

@Component
public class RealTime implements MorakTime {

    private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    private Clock clock = Clock.systemDefaultZone();

    @Override
    public void changeTime(LocalDateTime dateTime) {
        this.clock = Clock.fixed(dateTime.atOffset(zoneOffset).toInstant(), zoneOffset);
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}
