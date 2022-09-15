package com.morak.back.appointment.domain.timeconditions.durationtime;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationTime {
    private static final int MAX_HOURS = 24;
    private static final int MIN_HOURS = 0;
    private static final int MIN_TIME = 0;
    private static final int MAX_MINUTES = 59;
    private static final int HOUR_MINUTES = 60;
    private static final int DAY_MINUTES = 1440;

    @NotNull(message = "약속잡기 진행 시간은 null일 수 없습니다.")
    private Integer durationMinutes;

    public static DurationTime of(int hours, int minutes, int minuteUnit) {
        validateHoursRange(hours);
        validateMinuteRange(minutes);
        validateMinuteUnit(minutes, minuteUnit);
        int durationMinutes = hours * HOUR_MINUTES + minutes;
        validateDurationMinutesRange(durationMinutes, minuteUnit);
        return new DurationTime(durationMinutes);
    }

    private static void validateHoursRange(int hours) {
        if (hours < MIN_HOURS || hours > MAX_HOURS) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_HOUR_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기 진행시간 %d 는 0~24시 사이여야 합니다.", hours
                    )
            );
        }
    }

    private static void validateMinuteRange(int minutes) {
        if (minutes < MIN_TIME || minutes > MAX_MINUTES) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_MINUTE_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기 진행시간 %d 는 0~59분 사이여야 합니다.", minutes
                    )
            );
        }
    }

    private static void validateMinuteUnit(int minutes, int minuteUnit) {
        if (minutes % minuteUnit != 0) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR,
                    String.format(
                            "약속잡기 진행시간 %d 는 %d 분 단위여야 합니다.",
                            minutes, minuteUnit
                    )
            );
        }
    }

    private static void validateDurationMinutesRange(int durationMinutes, int minuteUnit) {
        if (durationMinutes < minuteUnit || durationMinutes > DAY_MINUTES) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_MINUTE_RANGE_ERROR,
                    String.format(
                            "약속잡기 진행 시간 %d 은 최소 %d 분에서 %d 분 사이여야 합니다.",
                            durationMinutes, minuteUnit, DAY_MINUTES
                    )
            );
        }
    }

    public boolean isBiggerThan(long durationMinute) {
        return this.durationMinutes > durationMinute;
    }

    public int parseHours() {
        return this.durationMinutes / HOUR_MINUTES;
    }

    public int parseMinutes() {
        return this.durationMinutes % HOUR_MINUTES;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        DurationTime that = (DurationTime) o;
        return Objects.equals(durationMinutes, that.durationMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationMinutes);

    }
}

