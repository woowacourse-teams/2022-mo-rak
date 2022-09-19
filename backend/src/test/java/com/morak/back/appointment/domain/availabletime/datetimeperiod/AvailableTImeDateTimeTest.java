package com.morak.back.appointment.domain.availabletime.datetimeperiod;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_TIME_PAST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AvailableTImeDateTimeTest {

    public static Stream<Arguments> availableTimeDateTime_isAfter() {
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of(AvailableTImeDateTime.of(now.plusMinutes(20), now), true),
                Arguments.of(AvailableTImeDateTime.of(now.plusMinutes(50), now), false)
        );
    }

    public static Stream<Arguments> localDateTime_isAfter() {
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of(now.plusMinutes(20), true),
                Arguments.of(now.plusMinutes(50), false)
        );
    }

    public static Stream<Arguments> localDateTime_isBefore() {
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of(now.plusMinutes(20), false),
                Arguments.of(now.plusMinutes(50), true)
        );
    }

    public static Stream<Arguments> availableTImeDateTime_isNotDividedBy() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 19, 12, 0, 0);

        return Stream.of(
                Arguments.of(AvailableTImeDateTime.of(now.plusMinutes(30), now), false),
                Arguments.of(AvailableTImeDateTime.of(now.plusMinutes(50), now), true)
        );
    }


    @Test
    void 현재시각보다_이전의_시각으로는_생성할_수_없다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.minusMinutes(1);

        //when & then
        assertThatThrownBy(() -> AvailableTImeDateTime.of(dateTime, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(AVAILABLETIME_TIME_PAST_ERROR);
    }

    @ParameterizedTest
    @MethodSource("availableTimeDateTime_isAfter")
    void 다른_dateTime보다_이후인지_확인한다(AvailableTImeDateTime other, boolean expected) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.plusMinutes(30);

        AvailableTImeDateTime availableTImeDateTime = AvailableTImeDateTime.of(dateTime, now);

        // when
        boolean actual = availableTImeDateTime.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    @ParameterizedTest
    @MethodSource("localDateTime_isAfter")
    void 다른_localDateTime보다_이후인지_확인한다(LocalDateTime other, boolean expected) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.plusMinutes(30);

        AvailableTImeDateTime availableTImeDateTime = AvailableTImeDateTime.of(dateTime, now);

        // when
        boolean actual = availableTImeDateTime.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("localDateTime_isBefore")
    void 다른_localDateTime보다_이전인지_확인한다(LocalDateTime other, boolean expected) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.plusMinutes(30);

        AvailableTImeDateTime availableTImeDateTime = AvailableTImeDateTime.of(dateTime, now);

        // when
        boolean actual = availableTImeDateTime.isBefore(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("availableTImeDateTime_isNotDividedBy")
    void 입력받은_시간으로_나눠지지_않는지_확인한다(AvailableTImeDateTime availableTImeDateTime, boolean expected) {
        // given
        int minutes = 30;

        // when
        boolean actual = availableTImeDateTime.isNotDividedBy(minutes);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 다른_availableTImeDateTime과의_시간차이를_계산한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.plusMinutes(30);

        AvailableTImeDateTime availableTImeDateTime = AvailableTImeDateTime.of(dateTime, now);

        // when
        long actual = availableTImeDateTime.calculateDurationMinute(AvailableTImeDateTime.of(now.plusMinutes(40), now));

        // then
        assertThat(actual).isEqualTo(10);
    }

    @Test
    void 날짜나_시간으로_변환한다() {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 9, 19, 7, 56, 0);
        LocalDateTime dateTime = now.plusMinutes(30);

        AvailableTImeDateTime availableTImeDateTime = AvailableTImeDateTime.of(dateTime, now);

        // when
        LocalDate localDate = availableTImeDateTime.toLocalDate();
        LocalTime localTime = availableTImeDateTime.toLocalTime();

        // then
        assertAll(
                () -> assertThat(localDate).isEqualTo(dateTime.toLocalDate()),
                () -> assertThat(localTime).isEqualTo(dateTime.toLocalTime())
        );
    }
}
