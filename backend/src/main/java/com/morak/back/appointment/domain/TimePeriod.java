package com.morak.back.appointment.domain;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.Duration;
import java.time.LocalTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.NotImplementedException;

@Getter
@NoArgsConstructor
@Embeddable
@ToString
public class TimePeriod {

    private static final int MINUTES_UNIT = 30;

    @AttributeOverrides(
            @AttributeOverride(name = "time", column = @Column(name = "start_time"))
    )
    private Time startTime;

    @AttributeOverrides(
            @AttributeOverride(name = "time", column = @Column(name = "end_time"))
    )
    private Time endTime;

    private TimePeriod(Time startTime, Time endTime) {
        validateChronology(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        this(new Time(startTime), new Time(endTime.minusMinutes(MINUTES_UNIT)));
    }

    private static void validateChronology(Time startTime, Time endTime) {
        if (endTime.isBefore(startTime)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR,
                    String.format(
                            "약속잡기 마지막 시간(%s)은 시작 시간(%s) 이후여야 합니다.",
                            endTime, startTime
                    )
            );
        }
    }

    public Duration getDuration() {
        return startTime.getDuration(endTime).plusMinutes(MINUTES_UNIT);
    }

    public boolean isAvailableRange(TimePeriod timePeriod) {
        throw new NotImplementedException();
    }

    public boolean isBetween(LocalTime localTime) {
        Time time = new Time(localTime);
        return !startTime.isAfter(time) && !endTime.isBefore(time);
    }
}
