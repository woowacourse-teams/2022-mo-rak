package com.morak.back.appointment.domain.appointment.timeconditions;

import com.morak.back.appointment.domain.appointment.timeconditions.appointmentdateperiod.AppointmentDatePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod.AppointmentTimePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.durationtime.DurationTime;
import com.morak.back.appointment.domain.appointment.timeconditions.period.DatePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.period.TimePeriod;
import com.morak.back.appointment.domain.availabletime.datetimeperiod.AvailableTimeDateTimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeConditions {

    public static final int MINUTE_UNIT = 30;

    @Embedded
    private DurationTime durationTime;

    @Embedded
    private AppointmentTimePeriod timePeriod;

    @Embedded
    private AppointmentDatePeriod datePeriod;

    public static TimeConditions of(LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime,
                                    Integer durationHours, Integer durationMinutes,
                                    LocalDate now) {

        DurationTime durationTime = DurationTime.of(durationHours, durationMinutes, MINUTE_UNIT);
        AppointmentTimePeriod timePeriod = AppointmentTimePeriod.of(startTime, endTime, durationTime, MINUTE_UNIT);
        AppointmentDatePeriod datePeriod = AppointmentDatePeriod.of(startDate, endDate, timePeriod.getEndTime(), now);

        return new TimeConditions(durationTime, timePeriod, datePeriod);
    }

    public LocalDateTime getEndDateTime() {
        return datePeriod.toEndDateTime(timePeriod.getEndTime());
    }

    public void validateDateTimePeriod(AvailableTimeDateTimePeriod availableTimeDateTimePeriod) {
        validateAvailableTimeRange(availableTimeDateTimePeriod);
        validateDividedByMinuteUnit(availableTimeDateTimePeriod);
        validateDateRange(availableTimeDateTimePeriod.toDatePeriod());
        validateTimeRange(availableTimeDateTimePeriod.toTimePeriod());
    }

    private void validateAvailableTimeRange(AvailableTimeDateTimePeriod availableTimeDateTimePeriod) {
        if (availableTimeDateTimePeriod.isDurationNotEquals(MINUTE_UNIT)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR,
                    String.format(
                            "약속잡기 가능 시간(%s~%s)은 %d 분이어야 합니다.",
                            availableTimeDateTimePeriod.getStartDateTime().getDateTime(),
                            availableTimeDateTimePeriod.getEndDateTime().toLocalDate(), MINUTE_UNIT
                    )
            );
        }
    }

    private void validateDividedByMinuteUnit(AvailableTimeDateTimePeriod availableTimeDateTimePeriod) {
        if (availableTimeDateTimePeriod.isNotDividedBy(MINUTE_UNIT)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR,
                    String.format(
                            "약속잡기 가능 시간(%s, %s)은 %d 분 단위여야 합니다.",
                            availableTimeDateTimePeriod.getStartDateTime(),
                            availableTimeDateTimePeriod.getEndDateTime(), MINUTE_UNIT
                    )
            );
        }
    }

    private void validateDateRange(DatePeriod datePeriod) {
        if (!this.datePeriod.contains(datePeriod)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기로 선택한 %s 는 약속잡기에 등록된 %s 이내여야 합니다.",
                            datePeriod,
                            this.datePeriod
                    )
            );
        }
    }

    private void validateTimeRange(TimePeriod timePeriod) {
        if (!this.timePeriod.contains(timePeriod)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기로 선택한 %s 는 %s 이내여야 합니다.",
                            timePeriod,
                            this.timePeriod
                    )
            );
        }
    }

    public int getDurationHours() {
        return durationTime.parseHours();
    }

    public int parseDurationMinutes() {
        return durationTime.parseMinutes();
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(getStartDate(), getStartTime());
    }

    public LocalDate getStartDate() {
        return this.datePeriod.getStartDate().getDate();
    }

    public LocalDate getEndDate() {
        return this.datePeriod.getEndDate().getDate();
    }

    public LocalTime getStartTime() {
        return this.timePeriod.getStartTime().getTime();
    }

    public LocalTime getEndTime() {
        return this.timePeriod.getEndTime().getTime();
    }


    public int getDurationTime() {
        return durationTime.getDurationMinutes();
    }
}
