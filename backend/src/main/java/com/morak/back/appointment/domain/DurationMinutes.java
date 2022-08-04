package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class DurationMinutes {

    private static final int HOUR_MINUTES = 60;
    private static final int MIN_TIME = 0;
    private static final int MAX_MINUTES = 59;
    private static final int MAX_HOURS = 24;
    private static final int DAY_MINUTES = 1440;

    @NotNull(message = "약속잡기 진행 시간은 null일 수 없습니다.")
    private Integer durationMinutes;

    public static DurationMinutes of(Integer hours, Integer minutes, int minutesUnit) {
        validateMinutes(minutes, minutesUnit);
        validateTimeFormat(hours, minutes);
        int durationMinutes = hours * HOUR_MINUTES + minutes;
        validateDurationMinutesRange(durationMinutes, minutesUnit);
        return new DurationMinutes(durationMinutes);
    }

    public Integer parseHours() {
        return this.durationMinutes / HOUR_MINUTES;
    }

    public Integer parseMinutes() {
        return this.durationMinutes % HOUR_MINUTES;
    }

    private static void validateMinutes(Integer minutes, int minutesUnit) {
        if (minutes % minutesUnit != 0) {
            throw new InvalidRequestException("약속잡기 진행 시간은 " + minutesUnit + "분 단위여야 합니다.");
        }
    }

    private static void validateTimeFormat(Integer hours, Integer minutes) {
        if (hours < MIN_TIME || hours > MAX_HOURS) {
            throw new InvalidRequestException("약속잡기 진행 시간은 0~24시 사이여야 합니다.");
        }

        if (minutes < MIN_TIME || minutes > MAX_MINUTES) {
            throw new InvalidRequestException("약속잡기 진행 시간은 0~59분 사이여야 합니다.");
        }
    }

    private static void validateDurationMinutesRange(Integer durationMinutes, int minutesUnit) {
        if (durationMinutes <= MIN_TIME || durationMinutes > DAY_MINUTES) {
            throw new InvalidRequestException("약속잡기 진행 시간은 최소 " + minutesUnit + "분에서 24시간 사이여야 합니다.");
        }
    }
}

