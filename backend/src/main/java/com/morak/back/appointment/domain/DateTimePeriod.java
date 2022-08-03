package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DateTimePeriod {

    @NotNull(message = "약속잡기 가능 시작 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 시작 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "약속잡기 가능 마지막 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 마지막 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime endDateTime;

    private DateTimePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static DateTimePeriod of(LocalDateTime startDateTime, LocalDateTime endDateTime, int minutesUnit) {
        validateChronology(startDateTime, endDateTime);
        validateMinutesUnit(startDateTime, endDateTime);
        validateAvailableTimeRange(startDateTime, endDateTime, minutesUnit);
        return new DateTimePeriod(startDateTime, endDateTime);
    }

    public DatePeriod toDatePeriod() {
        return new DatePeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
    }

    public TimePeriod toTimePeriod() {
        return TimePeriod.of(startDateTime.toLocalTime(), endDateTime.toLocalTime(), MINUTES_UNIT);
    }

    private static void validateChronology(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidRequestException("약속잡기 마지막 시점은 시작 시점 이후여야 합니다.");
        }
    }

    private static void validateMinutesUnit(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (isNotDividedByUnit(startDateTime) || isNotDividedByUnit(endDateTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 " + MINUTES_UNIT + "분 단위여야 합니다.");
        }
    }

    private static boolean isNotDividedByUnit(LocalDateTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    private static void validateAvailableTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                   int minutesUnit) {
        if (Duration.between(startDateTime, endDateTime).toMinutes() != minutesUnit) {
            throw new InvalidRequestException("약속잡기 가능 시간은 " + minutesUnit + "분이어야 합니다.");
        }
    }

    public boolean contains(DateTimePeriod other) {
        return !(other.startDateTime.isBefore(this.startDateTime) || other.endDateTime.isAfter(this.endDateTime));
    }

    public long getDurationUnitCount() {
        return Duration.between(this.startDateTime, this.endDateTime).toMinutes() / MINUTES_UNIT;
    }
}
