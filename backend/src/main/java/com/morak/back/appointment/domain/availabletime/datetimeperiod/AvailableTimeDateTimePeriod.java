package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import com.morak.back.appointment.domain.appointment.timeconditions.period.DatePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.period.DateTimePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.period.TimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailableTimeDateTimePeriod extends DateTimePeriod {

    @Embedded
    @NotNull(message = "약속잡기 가능 시작 시점은 null일 수 없습니다.")
    @AttributeOverride(name = "dateTime", column = @Column(name = "startDateTime"))
    private AvailableTImeDateTime startDateTime;

    @Embedded
    @NotNull(message = "약속잡기 가능 마지막 시점은 null일 수 없습니다.")
    @AttributeOverride(name = "dateTime", column = @Column(name = "endDateTime"))
    private AvailableTImeDateTime endDateTime;

    public static AvailableTimeDateTimePeriod of(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                 LocalDateTime now) {
        AvailableTImeDateTime availableTImeStartDateTime = AvailableTImeDateTime.of(startDateTime, now);
        AvailableTImeDateTime availableTImeEndDateTime = AvailableTImeDateTime.of(endDateTime, now);
        validateChronology(availableTImeStartDateTime, availableTImeEndDateTime);

        return new AvailableTimeDateTimePeriod(availableTImeStartDateTime, availableTImeEndDateTime);
    }

    private static void validateChronology(AvailableTImeDateTime startDateTime, AvailableTImeDateTime endDateTime) {
        if (!endDateTime.isAfter(startDateTime)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR,
                    String.format("약속잡기 마지막 시점(%s)는 시작 시점(%s) 이후여야 합니다.", endDateTime, startDateTime)
            );
        }
    }

    @Override
    @Transient
    public LocalDateTime getLocalStartDateTime() {
        return startDateTime.getDateTime();
    }

    @Override
    @Transient
    public LocalDateTime getLocalEndDateTime() {
        return endDateTime.getDateTime();
    }

    public boolean isNotDividedBy(int minute) {
        return startDateTime.isNotDividedBy(minute) || endDateTime.isNotDividedBy(minute);
    }

    public boolean isDurationNotEquals(int minute) {
        return startDateTime.calculateDurationMinute(endDateTime) != minute;
    }

    public DatePeriod toDatePeriod() {
        return new AvailableTimeDatePeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
    }

    public TimePeriod toTimePeriod() {
        return new AvailableTimeTimePeriod(startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }
}
