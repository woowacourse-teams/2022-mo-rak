package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

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

    public static final LocalTime ZERO_TIME = LocalTime.of(0, 0);

    /*
        startTime이 00:00일 경우 당일의 자정을 의미한다.
     */
    @NotNull(message = "약속잡기 시작 시간은 null일 수 없습니다.")
    private LocalTime startTime;

    /*
        endTime이 00:00일 경우 다음날의 자정을 의미한다.
     */
    @NotNull(message = "약속잡기 마지막 시간은 null일 수 없습니다.")
    private LocalTime endTime;

    private TimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimePeriod of(LocalTime startTime, LocalTime endTime, int minutesUnit) {
        if (!isMidnight(endTime)) {
            validateChronology(startTime, endTime);
        }
        validateMinutes(startTime, endTime, minutesUnit);

        return new TimePeriod(startTime, endTime);
    }

    private static boolean isMidnight(LocalTime endTime) {
        return endTime.equals(ZERO_TIME);
    }

    private static void validateChronology(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidRequestException("약속잡기 마지막 시간은 시작 시간 이후여야 합니다.");
        }
    }

    private static void validateMinutes(LocalTime startTime, LocalTime endTime, int minutesUnit) {
        if (isNotDividedByUnit(startTime) || isNotDividedByUnit(endTime)) {
            throw new InvalidRequestException("약속잡기 시작/마지막 시간은 " + minutesUnit + "분 단위여야 합니다.");
        }
    }

    private static boolean isNotDividedByUnit(LocalTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    public void validateAvailableTimeRange(TimePeriod timePeriod) {
        LocalTime selectedStartTime = timePeriod.startTime;
        if (selectedStartTime.isBefore(this.startTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 시간 이내여야 합니다.");
        }

        if (isMidnight(this.endTime)) {
            return;
        }

        LocalTime selectedEndTime = timePeriod.endTime;
        if (isOutOfEndTime(selectedEndTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 시간 이내여야 합니다.");
        }
    }

    private boolean isOutOfEndTime(LocalTime selectedEndTime) {
        return selectedEndTime.isAfter(this.endTime) || isMidnight(selectedEndTime);
    }
}
