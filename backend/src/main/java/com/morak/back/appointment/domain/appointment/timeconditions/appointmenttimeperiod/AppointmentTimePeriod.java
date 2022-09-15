package com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod;

import com.morak.back.appointment.domain.appointment.timeconditions.durationtime.DurationTime;
import com.morak.back.appointment.domain.appointment.timeconditions.period.TimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentTimePeriod extends TimePeriod {

    /*
        startTime이 00:00일 경우 당일의 자정을 의미한다.
     */
    @NotNull(message = "약속잡기 시작 시간은 null일 수 없습니다.")
    @Embedded
    @AttributeOverride(name = "time", column = @Column(name = "startTime"))
    private AppointmentTime startTime;

    /*
        endTime이 00:00일 경우 다음날의 자정을 의미한다.
     */
    @NotNull(message = "약속잡기 마지막 시간은 null일 수 없습니다.")
    @Embedded
    @AttributeOverride(name = "time", column = @Column(name = "endTime"))
    private AppointmentTime endTime;

    public static AppointmentTimePeriod of(LocalTime startTime, LocalTime endTime, DurationTime durationTime,
                                           int minuteUnit) {
        AppointmentTime appointmentStartTime = AppointmentTime.of(startTime, minuteUnit);
        AppointmentTime appointmentEndTime = AppointmentTime.of(endTime, minuteUnit);

        validateMidNight(appointmentStartTime, appointmentEndTime);
        validateDurationTime(appointmentStartTime, appointmentEndTime, durationTime);
        return new AppointmentTimePeriod(appointmentStartTime, appointmentEndTime);
    }

    private static void validateMidNight(AppointmentTime appointmentStartTime, AppointmentTime appointmentEndTime) {
        if (!appointmentEndTime.isMidNight()) {
            validateChronology(appointmentStartTime, appointmentEndTime);
        }
    }

    private static void validateChronology(AppointmentTime startTime, AppointmentTime endTime) {
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

    private static void validateDurationTime(AppointmentTime startTime, AppointmentTime endTime,
                                             DurationTime durationTime) {
        long durationMinute = endTime.minus(startTime);
        if (durationTime.isBiggerThan(durationMinute)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR,
                    String.format(
                            "진행시간(%d)은 약속잡기의 시작시간~마지막시간(%s ~ %s) 보다 짧아야 합니다.",
                            durationMinute, startTime.getTime(), endTime.getTime()
                    )
            );
        }
    }

    @Override
    public boolean contains(TimePeriod other) {
        boolean isValidStartTime = !startTime.isAfter(other.getLocalStartTime());

        if (endTime.isMidNight()) {
            return isValidStartTime;
        }

        return isValidStartTime && !endTime.isBefore(other.getLocalEndTime());
    }

    @Override
    public LocalTime getLocalStartTime() {
        return startTime.getTime();
    }

    @Override
    public LocalTime getLocalEndTime() {
        return endTime.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        AppointmentTimePeriod that = (AppointmentTimePeriod) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}


