package com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentTime {

    private LocalTime time;

    public static AppointmentTime of(LocalTime time, int minuteUnit) {
        validateMinutes(time, minuteUnit);
        return new AppointmentTime(time);
    }

    private static void validateMinutes(LocalTime time, int minuteUnit) {
        if (isNotDividedByUnit(time, minuteUnit)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR,
                    String.format(
                            "약속잡기 시작 또는 마지막 시간(%s)은 %d분 단위여야 합니다.",
                            time, minuteUnit
                    )
            );
        }
    }

    private static boolean isNotDividedByUnit(LocalTime time, int minuteUnit) {
        return time.getMinute() % minuteUnit != 0;
    }

    @Transient
    public boolean isMidNight() {
        return this.time.equals(LocalTime.MIDNIGHT);
    }

    public boolean isAfter(AppointmentTime other) {
        return isAfter(other.time);
    }

    public boolean isAfter(LocalTime time) {
        return this.time.isAfter(time);
    }

    public boolean isBefore(LocalTime time) {
        return this.time.isBefore(time);
    }

    public long minus(AppointmentTime other) {
        if (isMidNight()) {
            return Duration.between(other.time, this.time).plusDays(1).toMinutes();
        }
        return Duration.between(other.time, this.time).toMinutes();
    }
}
