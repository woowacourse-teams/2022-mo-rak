package com.morak.back.appointment.domain;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Date {

    private LocalDate date;

    public Date(LocalDate date, LocalDate today) {
        validateFutureOrPresent(date, today);
        this.date = date;
    }

    private void validateFutureOrPresent(LocalDate date, LocalDate today) {
        if (date.isBefore(today)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR,
                    String.format("약속잡기 날짜(%s)는 현재(%s)보다 과거일 수 없습니다.", date, today)
            );
        }
    }

    public boolean isAfter(Date other) {
        return this.date.isAfter(other.date);
    }

    public boolean isAfter(LocalDate other) {
        return this.date.isAfter(other);
    }

    public boolean isBefore(LocalDate other) {
        return this.date.isBefore(other);
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
