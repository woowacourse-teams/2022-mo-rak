package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DurationMinutes {

    private static final int HOUR_MINUTES = 60;
    private static final int MIN_TIME = 0;
    private static final int MAX_MINUTES = 59;
    private static final int MAX_HOURS = 24;
    private static final int MINUTES_UNIT = 30;
    private static final int REMAIN_ZERO = 0;
    private static final int DAY_MINUTES = 1440;

    private Integer durationMinutes;

    public DurationMinutes(Integer hours, Integer minutes) {
        validateMinutes(minutes);
        validateTimeFormat(hours, minutes);
        int durationMinutes = hours * HOUR_MINUTES + minutes;
        validateDurationMinutesRange(durationMinutes);
        this.durationMinutes = durationMinutes;
    }

    public Integer parseHours() {
        return this.durationMinutes / HOUR_MINUTES;
    }

    public Integer parseMinutes() {
        return this.durationMinutes % HOUR_MINUTES;
    }

    private void validateTimeFormat(Integer hours, Integer minutes) {
        if (hours < MIN_TIME || hours > MAX_HOURS) {
            throw new InvalidRequestException("약속잡기 진행 시간은 0~24시 사이여야 합니다.");
        }

        if (minutes < MIN_TIME || minutes > MAX_MINUTES) {
            throw new InvalidRequestException("약속잡기 진행 시간은 0~59분 사이여야 합니다.");
        }
    }

    private void validateMinutes(Integer minutes) {
        if (minutes % MINUTES_UNIT != REMAIN_ZERO) {
            throw new InvalidRequestException("약속잡기 진행 시간은 30분 단위여야 합니다.");
        }
    }

    private void validateDurationMinutesRange(Integer durationMinutes) {
        if (durationMinutes <=  MIN_TIME || durationMinutes > DAY_MINUTES) {
            throw new InvalidRequestException("약속잡기 진행 시간은 30분~24시간 사이여야 합니다.");
        }
    }
}

