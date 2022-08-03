package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DatePeriod {

    private static final long ONE_DAY = 1L;

    @NotNull(message = "약속잡기 시작 날짜는 null일 수 없습니다.")
    private LocalDate startDate;

    @NotNull(message = "약속잡기 마지막 날짜는 null일 수 없습니다.")
    private LocalDate endDate;

    protected DatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DatePeriod of(LocalDate startDate, LocalDate endDate, LocalTime endTime) {
        validateFutureOrPresent(startDate, endDate);
        validateChronology(startDate, endDate);
        if (endTime.equals(TimePeriod.ZERO_TIME)) {
            endDate = endDate.plusDays(ONE_DAY);
        }
        return new DatePeriod(startDate, endDate);
    }

    public void validateAvailableDateRange(DatePeriod other) {
        if (other.startDate.isBefore(this.startDate) || other.endDate.isAfter(this.endDate)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 날짜 이내여야 합니다.");
        }
    }

    private static void validateFutureOrPresent(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            throw new InvalidRequestException("약속잡기 날짜는 과거일 수 없습니다.");
        }
    }

    private static void validateChronology(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException("약속잡기 마지막 날짜는 시작 날짜 이후여야 합니다.");
        }
    }
}
