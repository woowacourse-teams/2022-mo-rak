package com.morak.back.appointment.domain.recommand;

import com.morak.back.appointment.domain.appointment.timeconditions.period.DateTimePeriod;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RecommendationDateTimePeriod extends DateTimePeriod {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public RecommendationDateTimePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public boolean contains(DateTimePeriod other) {
        return !startDateTime.isAfter(other.getLocalStartDateTime())
                && !endDateTime.isBefore(other.getLocalEndDateTime());
    }

    public long countDurationUnit(int minute) {
        return Duration.between(this.startDateTime, this.endDateTime).toMinutes() / minute;
    }

    public RecommendationDateTimePeriod getNextPeriod(int minute) {
        return new RecommendationDateTimePeriod(startDateTime.plusMinutes(minute), endDateTime.plusMinutes(minute));
    }

    @Override
    public LocalDateTime getLocalStartDateTime() {
        return startDateTime;
    }

    @Override
    public LocalDateTime getLocalEndDateTime() {
        return endDateTime;
    }
}
