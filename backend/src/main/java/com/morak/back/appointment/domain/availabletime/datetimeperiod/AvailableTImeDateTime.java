package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailableTImeDateTime {

    private LocalDateTime dateTime;


    public static AvailableTImeDateTime of(LocalDateTime dateTime, LocalDateTime now) {
        validateNow(dateTime, now);
        return new AvailableTImeDateTime(dateTime);
    }

    private static void validateNow(LocalDateTime dateTime, LocalDateTime now) {
        if (dateTime.isBefore(now)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_TIME_PAST_ERROR,
                    String.format(
                            "약속잡기 (%s)는 현재(%s)보다 과거일 수 없습니다.",
                            dateTime, now
                    )
            );
        }
    }

    public boolean isAfter(AvailableTImeDateTime other) {
        return isAfter(other.dateTime);
    }

    public boolean isAfter(LocalDateTime dateTime) {
        return this.dateTime.isAfter(dateTime);
    }

    public boolean isBefore(LocalDateTime dateTime) {
        return this.dateTime.isBefore(dateTime);
    }

    public boolean isNotDividedBy(int minute) {
        return this.dateTime.getMinute() % minute != 0;
    }

    public long calculateDurationMinute(AvailableTImeDateTime other) {
        return Duration.between(this.dateTime, other.dateTime).toMinutes();
    }

    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }
}
