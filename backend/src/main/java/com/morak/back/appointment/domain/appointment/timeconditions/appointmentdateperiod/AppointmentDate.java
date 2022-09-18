package com.morak.back.appointment.domain.appointment.timeconditions.appointmentdateperiod;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentDate {

    private LocalDate date;

    public static AppointmentDate of(LocalDate date, LocalDate now) {
        validateNow(date, now);
        return new AppointmentDate(date);
    }

    private static void validateNow(LocalDate date, LocalDate now) {
        if (date.isBefore(now)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR,
                    String.format(
                            "약속잡기 날짜(%s)는 현재(%s)보다 과거일 수 없습니다.",
                            date, now
                    )
            );
        }
    }

    public boolean isBefore(LocalDate date) {
        return this.date.isBefore(date);
    }

    public boolean isAfter(AppointmentDate other) {
        return isAfter(other.date);
    }

    public boolean isAfter(LocalDate date) {
        return this.date.isAfter(date);
    }

    public void plusDays(long oneDay) {
        date = date.plusDays(oneDay);
    }
}
