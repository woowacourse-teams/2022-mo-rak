package com.morak.back.appointment.domain;

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

    private static final int MINUTES_UNIT = 30;

    @NotNull(message = "약속잡기 가능 시작 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 시작 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "약속잡기 가능 마지막 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 마지막 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime endDateTime;

    public DateTimePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateChronology(startDateTime, endDateTime);
        validateMinutesUnit(startDateTime, endDateTime);
        validateAvailableTimeRange(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void validateChronology(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidRequestException("약속잡기 마지막 시점은 시작 시점 이후여야 합니다.");
        }
    }

    private void validateMinutesUnit(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (isNotDividedByUnit(startDateTime) || isNotDividedByUnit(endDateTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 30분 단위여야 합니다.");
        }
    }

    private boolean isNotDividedByUnit(LocalDateTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    private void validateAvailableTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (Duration.between(startDateTime, endDateTime).toMinutes() != MINUTES_UNIT) {
            throw new InvalidRequestException("약속잡기 가능 시간은 30분이어야 합니다.");
        }
    }
}
