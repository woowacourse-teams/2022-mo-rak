package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DatePeriod {
    
    @NotNull(message = "약속잡기 시작 날짜는 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 시작 날짜는 현재보다 과거일 수 없습니다.")
    private LocalDate startDate;

    @NotNull(message = "약속잡기 마지막 날짜는 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 마지막 날짜는 현재보다 과거일 수 없습니다.")
    private LocalDate endDate;

    public DatePeriod(LocalDate startDate, LocalDate endDate) {
        validateChronology(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateChronology(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException("약속잡기 마지막 날짜는 시작 날짜 이후여야 합니다.");
        }
    }
}
