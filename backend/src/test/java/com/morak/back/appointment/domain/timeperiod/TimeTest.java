package com.morak.back.appointment.domain.timeperiod;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.Duration;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeTest {

    @Test
    void 삼십분_단위로_나눠지지_않는_시각이_들어오면_예외를_던진다() {
        // given
        LocalTime time = LocalTime.of(12, 1);

        // when & then
        assertThatThrownBy(() -> new Time(time))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR);
    }

    @ParameterizedTest
    @CsvSource(value = {"-30,false", "0,false", "30,true"})
    void 다른_시각_객체보다_이전인지_확인한다(int plusMinutes, boolean expected) {
        // given
        LocalTime now = LocalTime.of(12, 0);
        LocalTime other = now.plusMinutes(plusMinutes);
        Time time = new Time(now);

        // when
        boolean actual = time.isBefore(new Time(other));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"-30,false", "0,false", "30,true"})
    void 다른_시각_보다_이전인지_확인한다(int plusMinutes, boolean expected) {
        // given
        LocalTime now = LocalTime.of(12, 0);
        LocalTime other = now.plusMinutes(plusMinutes);
        Time time = new Time(now);

        // when
        boolean actual = time.isBefore(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"-30,true", "0,false", "30,false"})
    void 다른_시각_객체보다_이후인지_확인한다(int plusMinutes, boolean expected) {
        // given
        LocalTime now = LocalTime.of(12, 0);
        LocalTime other = now.plusMinutes(plusMinutes);
        Time time = new Time(now);

        // when
        boolean actual = time.isAfter(new Time(other));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"-30,true", "0,false", "30,false"})
    void 다른_시각_보다_이후인지_확인한다(int plusMinutes, boolean expected) {
        // given
        LocalTime now = LocalTime.of(12, 0);
        LocalTime other = now.plusMinutes(plusMinutes);
        Time time = new Time(now);

        // when
        boolean actual = time.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 다른_시각과의_차이를_구한다() {
        // given
        LocalTime now = LocalTime.of(0, 0);
        LocalTime other = LocalTime.of(11, 30);
        Time time = new Time(now);
        Time otherTime = new Time(other);

        // when
        Duration duration = time.getDuration(otherTime);

        // then
        assertThat(duration).isEqualTo(Duration.ofMinutes(11 * 60 + 30));
    }
}
