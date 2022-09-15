package com.morak.back.core.domain.times;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

@Component
public class LocalTimes implements Times {

    private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    private Clock clock = Clock.systemDefaultZone();

    @Override
    public LocalDateTime dateTimeOfNow() {
        return LocalDateTime.now(clock);
    }

    @Override
    public LocalDate dateOfNow() {
        return LocalDate.now(clock);
    }

    @Override
    public void changeTime(LocalDateTime dateTime) {
        clock = Clock.fixed(dateTime.atOffset(zoneOffset).toInstant(), zoneOffset);
    }

    @Override
    public void reset() {
        changeTime(LocalDateTime.now());
    }

}
