package com.morak.back.appointment.domain;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
public class DatePeriod {

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "date", column = @Column(name = "start_date"))
    )
    private Date startDate;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "date", column = @Column(name = "end_date"))
    )
    private Date endDate;

    private DatePeriod(Date startDate, Date endDate) {
        validateChronology(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DatePeriod(LocalDate startDate, LocalDate endDate, LocalDate today) {
        this(new Date(startDate, today), new Date(endDate, today));
    }

    private static void validateChronology(Date startDate, Date endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR,
                    String.format("약속잡기 마지막 날짜(%s)는 시작 날짜(%s) 이후여야 합니다.", endDate, startDate)
            );
        }
    }

    public boolean isBetween(LocalDate date) {
        return !this.startDate.isAfter(date) && !this.endDate.isBefore(date);
    }

    public LocalDate getStartDate() {
        return startDate.getDate();
    }

    public LocalDate getEndDate() {
        return endDate.getDate();
    }
}
