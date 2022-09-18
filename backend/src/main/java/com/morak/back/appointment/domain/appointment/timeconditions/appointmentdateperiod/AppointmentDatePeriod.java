package com.morak.back.appointment.domain.appointment.timeconditions.appointmentdateperiod;

import com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod.AppointmentTime;
import com.morak.back.appointment.domain.appointment.timeconditions.period.DatePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class AppointmentDatePeriod extends DatePeriod {

    private static final long ONE_DAY = 1L;

    @NotNull(message = "약속잡기 시작 날짜는 null일 수 없습니다.")
    @AttributeOverride(name = "date", column = @Column(name = "startDate"))
    private AppointmentDate startDate;

    @NotNull(message = "약속잡기 마지막 날짜는 null일 수 없습니다.")
    @AttributeOverride(name = "date", column = @Column(name = "endDate"))
    private AppointmentDate endDate;

    @Transient
    private boolean isMidNight;

    public static AppointmentDatePeriod of(LocalDate startDate, LocalDate endDate,
                                           AppointmentTime endTime, LocalDate now) {

        AppointmentDate appointmentStartDate = AppointmentDate.of(startDate, now);
        AppointmentDate appointmentEndDate = AppointmentDate.of(endDate, now);

        validateChronology(appointmentStartDate, appointmentEndDate);
        boolean isMidNight = isMidNight(endTime, appointmentEndDate);

        return new AppointmentDatePeriod(appointmentStartDate, appointmentEndDate, isMidNight);
    }


    private static void validateChronology(AppointmentDate startDate, AppointmentDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR,
                    String.format(
                            "약속잡기 마지막 날짜(%s)는 시작 날짜(%s) 이후여야 합니다.",
                            endDate.getDate(), startDate.getDate()
                    )
            );
        }
    }

    private static boolean isMidNight(AppointmentTime endTime, AppointmentDate appointmentEndDate) {
        if (endTime.isMidNight()) {
            appointmentEndDate.plusDays(ONE_DAY);
            return true;
        }
        return false;
    }

    public LocalDateTime toEndDateTime(AppointmentTime endTime) {
        return LocalDateTime.of(endDate.getDate(), endTime.getTime());
    }

    @Override
    public boolean contains(DatePeriod other) {

        boolean isValidStartDate = !startDate.isAfter(other.getLocalStartDate());
        boolean isValidStartDateWhenIsMidNight = isMidNight && endDate.isAfter(other.getLocalStartDate());
        boolean isValidStartDateWhenIsNotMidNight = !isMidNight && !endDate.isBefore(other.getLocalEndDate());

        boolean isValidEndDate = isValidStartDateWhenIsMidNight || isValidStartDateWhenIsNotMidNight;

        return isValidStartDate && isValidEndDate;
    }

    @Override
    public LocalDate getLocalStartDate() {
        return startDate.getDate();
    }

    @Override
    public LocalDate getLocalEndDate() {
        return endDate.getDate();
    }
}


