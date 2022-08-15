package com.morak.back.appointment.domain;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@ToString
public class DatePeriod {

    private static final long ONE_DAY = 1L;

    @NotNull(message = "약속잡기 시작 날짜는 null일 수 없습니다.")
    private LocalDate startDate;

    @NotNull(message = "약속잡기 마지막 날짜는 null일 수 없습니다.")
    private LocalDate endDate;

    public static DatePeriod of(LocalDate startDate, LocalDate endDate, LocalTime endTime) {
        validateFutureOrPresent(startDate, endDate);
        validateChronology(startDate, endDate);
        if (endTime.equals(LocalTime.MIDNIGHT)) {
            endDate = endDate.plusDays(ONE_DAY);
        }
        return new DatePeriod(startDate, endDate);
    }

    private static void validateFutureOrPresent(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR,
                String.format(
                    "약속잡기 날짜(%s, %s)는 과거일 수 없습니다.",
                    startDate, endDate
                )
            );
        }
    }

    private static void validateChronology(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR,
                String.format(
                    "약속잡기 마지막 날짜(%s)는 시작 날짜(%s) 이후여야 합니다.",
                    endDate, startDate
                )
            );
        }
    }

    public boolean isAvailableRange(DatePeriod other) {
        return !(other.startDate.isBefore(this.startDate) || other.endDate.isAfter(this.endDate));
    }
}
