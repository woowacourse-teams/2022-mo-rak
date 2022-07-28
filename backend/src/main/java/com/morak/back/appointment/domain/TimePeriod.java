package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class TimePeriod {

    private static final int MINUTES_UNIT = 30;
    private static final int REMAIN_ZERO = 0;

    @NotNull(message = "약속잡기 시작 시간은 null일 수 없습니다.")
    private LocalTime startTime;

    @NotNull(message = "약속잡기 마지막 시간은 null일 수 없습니다.")
    private LocalTime endTime;

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        validateMinutes(startTime, endTime);
        validateChronology(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateMinutes(LocalTime startTime, LocalTime endTime) {
        if (isNotDividedByUnit(startTime) || isNotDividedByUnit(endTime)) {
            throw new InvalidRequestException("약속잡기 시작/마지막 시간은 30분 단위여야 합니다.");
        }
    }

    private boolean isNotDividedByUnit(LocalTime time) {
        return time.getMinute() % MINUTES_UNIT != REMAIN_ZERO;
    }

    private void validateChronology(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidRequestException("약속잡기 마지막 시간은 시작 시간 이후여야 합니다.");
        }
    }

    public void validateAvailableTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime.isBefore(this.startTime) || endTime.isAfter(this.endTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 시간 이내여야 합니다.");
        }
    }
}
