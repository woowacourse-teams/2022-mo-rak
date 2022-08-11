package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Embeddable
@ToString
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

    public static TimePeriod of(LocalTime startTime, LocalTime endTime) {
        if (!isMidnight(endTime)) {
            validateChronology(startTime, endTime);
        }
        validateMinutes(startTime, endTime);
        return new TimePeriod(startTime, endTime);
    }

    private static boolean isMidnight(LocalTime endTime) {
        return endTime.equals(ZERO_TIME);
    }

    private static void validateChronology(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR,
                String.format(
                    "약속잡기 마지막 시간(%s)은 시작 시간(%s) 이후여야 합니다.",
                    endTime, startTime
                )
            );
        }
    }

    // TODO : minutesUnit. 상수인가, 변수인가
    private static void validateMinutes(LocalTime startTime, LocalTime endTime) {
        if (isNotDividedByUnit(startTime) || isNotDividedByUnit(endTime)) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR,
                String.format(
                    "약속잡기 시작/마지막 시간(%s, %s)은 %d분 단위여야 합니다.",
                    startTime, endTime, MINUTES_UNIT
                )
            );
        }

    }

    private static boolean isNotDividedByUnit(LocalTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    public boolean isAvailableRange(TimePeriod timePeriod) {
        LocalTime selectedStartTime = timePeriod.startTime;
        if (selectedStartTime.isBefore(this.startTime)) {
            return false;
        }

        if (isMidnight(this.endTime)) {
            return true;
        }

        LocalTime selectedEndTime = timePeriod.endTime;
        return !isOutOfEndTime(selectedEndTime);
    }

    private boolean isOutOfEndTime(LocalTime selectedEndTime) {
        return selectedEndTime.isAfter(this.endTime) || isMidnight(selectedEndTime);
    }

    public boolean isLessThanDurationMinutes(Integer durationMinutes) {
        if (endTime.equals(ZERO_TIME)) {
            return Duration.between(startTime, endTime).plusDays(1).toMinutes() < durationMinutes;
        }
        return Duration.between(startTime, endTime).toMinutes() < durationMinutes;
    }
}
