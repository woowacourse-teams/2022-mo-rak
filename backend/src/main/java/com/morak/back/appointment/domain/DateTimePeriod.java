package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: 2022/08/04 this 객체는 available time 객체와 recommend 로직에서 모두 쓰임! 수정 필요할 것 같다는 엘리의 진단!
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
@ToString
public class DateTimePeriod {

    @NotNull(message = "약속잡기 가능 시작 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 시작 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "약속잡기 가능 마지막 시점은 null일 수 없습니다.")
    @FutureOrPresent(message = "약속잡기 가능 마지막 시점은 현재보다 과거일 수 없습니다.")
    private LocalDateTime endDateTime;

    /*
    `DateTimePeriod`는 `Recommendation`에서도 사용하기 때문에 `minutesUnit`이 필요하다.
     */
    public static DateTimePeriod of(LocalDateTime startDateTime, LocalDateTime endDateTime, int minutesUnit) {
        validateChronology(startDateTime, endDateTime);
        validateAvailableTimeRange(startDateTime, endDateTime, minutesUnit);
        validateMinutesUnit(startDateTime, endDateTime);
        return new DateTimePeriod(startDateTime, endDateTime);
    }

    public boolean contains(DateTimePeriod other) {
        return !(other.startDateTime.isBefore(this.startDateTime) || other.endDateTime.isAfter(this.endDateTime));
    }

    public long countDurationUnit() {
        return Duration.between(this.startDateTime, this.endDateTime).toMinutes() / MINUTES_UNIT;
    }

    private static void validateChronology(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR,
                String.format(
                    "약속잡기 마지막 시점(%s)는 시작 시점(%s) 이후여야 합니다.",
                    endDateTime, startDateTime
                )
            );
        }
    }

    /**
     * Duration이 30분이 아닌 경우를 검증한다.
     */
    private static void validateAvailableTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                   int minutesUnit) {
        if (Duration.between(startDateTime, endDateTime).toMinutes() != minutesUnit) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR,
                String.format(
                    "약속잡기 가능 시간(%s~%s)은 " + minutesUnit + "분이어야 합니다.",
                    startDateTime, endDateTime
                )
            );
        }
    }

    /**
     20분 ~ 50분인 경우를 검증한다.
     */
    private static void validateMinutesUnit(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (isNotDividedByUnit(startDateTime) || isNotDividedByUnit(endDateTime)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR,
                    String.format(
                            "약속잡기 가능 시간(%s, %s)은 " + MINUTES_UNIT + "분 단위여야 합니다.",
                            startDateTime, endDateTime
                    )
            );
        }
    }

    private static boolean isNotDividedByUnit(LocalDateTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    public DatePeriod toDatePeriod(LocalTime localTime) {
        LocalDate endDate = endDateTime.toLocalDate();
        if (localTime.equals(LocalTime.of(0, 0))) {
            endDate = endDate.minusDays(1);
        }
        return new DatePeriod(startDateTime.toLocalDate(), endDate);
    }

    public TimePeriod toTimePeriod() {
        return TimePeriod.of(startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }

}
