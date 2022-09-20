package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.morak.back.appointment.domain.appointment.timeconditions.period.DatePeriod;
import com.morak.back.appointment.domain.appointment.timeconditions.period.TimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AvailableTimeDateTimePeriodTest {

    public static Stream<Arguments> isNotDividedBy() {
        return Stream.of(
                Arguments.of(30, false),
                Arguments.of(10, false),
                Arguments.of(59, true)
        );
    }

    public static Stream<Arguments> isDurationNotEquals() {
        return Stream.of(
                Arguments.of(30, false),
                Arguments.of(10, true),
                Arguments.of(59, true)
        );
    }

    @Test
    void availableTime의_시작_시간이_끝_시간보다_이후이면_예외를_던진다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusHours(1);
        LocalDateTime endDateTime = startDateTime.minusMinutes(30);

        // when & then
        assertThatThrownBy(() -> AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR);
    }

    @ParameterizedTest
    @MethodSource("isNotDividedBy")
    void 주어진_minute으로_나눠지지_않는_지_확인한다(int minute, boolean expected) {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 9, 20, 16,0,0);
        LocalDateTime startDateTime = now.plusHours(1);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        AvailableTimeDateTimePeriod dateTimePeriod = AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, now);

        // when
        boolean actual = dateTimePeriod.isNotDividedBy(minute);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    @ParameterizedTest
    @MethodSource("isDurationNotEquals")
    void 시작_시간과_끝_시간의_차이가_주어진_minute과_일치하지_않는지_확인한다(int minute, boolean expected) {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 9, 20, 16,0,0);
        LocalDateTime startDateTime = now.plusHours(1);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        AvailableTimeDateTimePeriod dateTimePeriod = AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, now);

        // when
        boolean actual = dateTimePeriod.isDurationNotEquals(minute);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 약속잡기_선택_기간을_날짜_기간과_시간_기간으로_분리할_수_있다() {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 9, 20, 16,0,0);
        LocalDateTime startDateTime = now.plusHours(1);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        AvailableTimeDateTimePeriod dateTimePeriod = AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, now);

        // when
        DatePeriod datePeriod = dateTimePeriod.toDatePeriod();
        TimePeriod timePeriod = dateTimePeriod.toTimePeriod();

        // then
        assertAll(
                () -> assertThat(datePeriod.getLocalStartDate()).isEqualTo(startDateTime.toLocalDate()),
                () -> assertThat(datePeriod.getLocalEndDate()).isEqualTo(endDateTime.toLocalDate()),
                () -> assertThat(timePeriod.getLocalStartTime()).isEqualTo(startDateTime.toLocalTime()),
                () -> assertThat(timePeriod.getLocalEndTime()).isEqualTo(endDateTime.toLocalTime())
        );
    }
}
