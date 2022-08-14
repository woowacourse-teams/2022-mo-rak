package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class DurationMinutes {

    private static final int HOUR_MINUTES = 60;
    private static final int MIN_TIME = 0;
    private static final int MAX_MINUTES = 59;
    private static final int MAX_HOURS = 24;
    private static final int DAY_MINUTES = 1440;

    @NotNull(message = "약속잡기 진행 시간은 null일 수 없습니다.")
    private Integer durationMinutes;

    public static DurationMinutes of(Integer hours, Integer minutes) {
        validateTimeFormat(hours, minutes);
        validateMinutes(minutes);
        int durationMinutes = hours * HOUR_MINUTES + minutes;
        validateDurationMinutesRange(durationMinutes);
        return new DurationMinutes(durationMinutes);
    }

    public Integer parseHours() {
        return this.durationMinutes / HOUR_MINUTES;
    }

    public Integer parseMinutes() {
        return this.durationMinutes % HOUR_MINUTES;
    }

    private static void validateTimeFormat(Integer hours, Integer minutes) {
        if (hours < MIN_TIME || hours > MAX_HOURS) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_HOUR_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기 진행시간 %d 는 0~24시 사이여야 합니다.", hours
                    )

            );
        }

        if (minutes < MIN_TIME || minutes > MAX_MINUTES) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_MINUTE_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기 진행시간 %d 는 0~59분 사이여야 합니다.", minutes
                    )
            );
        }
    }

    private static void validateMinutes(Integer minutes) {
        if (minutes % MINUTES_UNIT != 0) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR,
                String.format(
                    "약속잡기 진행시간 %d 는 %d 분 단위여야 합니다.",
                    minutes, MINUTES_UNIT
                )
            );
        }
    }

    private static void validateDurationMinutesRange(Integer durationMinutes) {
        if (durationMinutes < MINUTES_UNIT || durationMinutes > DAY_MINUTES) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_DURATION_MINUTE_RANGE_ERROR,
                String.format(
                    "약속잡기 진행 시간 %d 은 최소 %d 분에서 %d 분 사이여야 합니다.",
                    durationMinutes, MINUTES_UNIT, DAY_MINUTES
                )
            );
        }
    }
}

