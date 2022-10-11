package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Time {

    private LocalTime time;

    public Time(LocalTime time) {
        validateMinute(time);
        this.time = time;
    }

    private void validateMinute(LocalTime time) {
        if (!isDividedByUnit(time)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR,
                    String.format("약속잡기의 시각(%s)은 %d분 단위여야 합니다.", time, MINUTES_UNIT)
            );
        }
    }

    private boolean isDividedByUnit(LocalTime time) {
        return time.getMinute() % MINUTES_UNIT == 0;
    }

    public boolean isBefore(Time other) {
        return this.isBefore(other.time);
    }

    public boolean isBefore(LocalTime other) {
        return this.time.isBefore(other);
    }

    public boolean isAfter(Time other) {
        return this.isAfter(other.time);
    }

    public boolean isAfter(LocalTime other) {
        return this.time.isAfter(other);
    }

    public Duration getDuration(Time other) {
        return Duration.between(this.time, other.time);
    }
}
