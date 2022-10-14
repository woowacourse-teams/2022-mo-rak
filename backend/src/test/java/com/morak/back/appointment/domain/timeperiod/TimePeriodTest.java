package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.timeperiod.TimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class TimePeriodTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_시작_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, minutes), LocalTime.of(14, 0)))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_마지막_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, 30), LocalTime.of(14, minutes)))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR);
    }

    @Test
    void 약속잡기_마지막_시간이_자정일_경우_생성시_시간순_검증을_하지_않는다() {
        // given
        LocalTime midnight = LocalTime.of(0, 0);

        // when & then
        assertThatNoException().isThrownBy(
                () -> new TimePeriod(LocalTime.of(0, 0), midnight)
        );
    }

    @Test
    void 약속잡기_마지막_시간이_시작_시간보다_빠를경우_예외를_던진다() {
        // given
        int startTimeHour = 10;
        int endTimeHour = 5;
        // when & then
        assertThatThrownBy(() -> new TimePeriod(
                LocalTime.of(startTimeHour, 0),
                LocalTime.of(endTimeHour, 0)))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR);

    }

    @ParameterizedTest
    @CsvSource({"0, 0, false", "9, 30, false", "19, 0, true", "23, 30, false"})
    void 약속잡기_시간이_10시부터_20시인_경우_포함되는지_확인한다(int hour, int minute, boolean expected) {
        // given
        TimePeriod appointmentTimePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(20, 0));

        // when
        boolean isBetween = appointmentTimePeriod.isBetween(LocalTime.of(hour, minute));

        // then
        assertThat(isBetween).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"0, 0, false", "9, 30, false", "10, 0, true", "19, 0, true", "23, 30, true"})
    void 약속잡기_시간이_10시부터_24시인_경우_포함되는지_확인한다(int hour, int minute, boolean expected) {
        // given
        TimePeriod appointmentTimePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(0, 0));

        // when
        boolean isBetween = appointmentTimePeriod.isBetween(LocalTime.of(hour, minute));

        // then
        assertThat(isBetween).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"0, 0, true", "10, 0, true", "19, 0, true", "23, 30, true"})
    void 약속잡기_시간이_0시부터_24시인_경우_포함되는지_확인한다(int hour, int minute, boolean expected) {
        // given
        TimePeriod appointmentTimePeriod = new TimePeriod(LocalTime.of(0, 0), LocalTime.of(0, 0));

        // when
        boolean isBetween = appointmentTimePeriod.isBetween(LocalTime.of(hour, minute));

        // then
        assertThat(isBetween).isEqualTo(expected);
    }
}
