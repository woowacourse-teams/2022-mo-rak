package com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppointmentTimeTest {

    public static Stream<Arguments> localDate() {
        return Stream.of(
                Arguments.of(LocalTime.of(1, 29)),
                Arguments.of(LocalTime.of(1, 31)),
                Arguments.of(LocalTime.of(1, 59)),
                Arguments.of(LocalTime.of(1, 1))
        );
    }

    public static Stream<Arguments> isMidNight() {
        return Stream.of(
                Arguments.of(LocalTime.of(12, 0), false),
                Arguments.of(LocalTime.of(1, 0), false),
                Arguments.of(LocalTime.of(0, 0), true)
        );
    }

    public static Stream<Arguments> appointmentTime_isAfter() {
        return Stream.of(
                Arguments.of(LocalTime.of(11, 0), true),
                Arguments.of(LocalTime.of(23, 0), false),
                Arguments.of(LocalTime.of(12, 0), false)
        );
    }

    public static Stream<Arguments> localTime_isAfter() {
        return Stream.of(
                Arguments.of(LocalTime.of(11, 0), true),
                Arguments.of(LocalTime.of(23, 0), false),
                Arguments.of(LocalTime.of(12, 0), false)
        );
    }

    public static Stream<Arguments> localTime_isBefore() {
        return Stream.of(
                Arguments.of(LocalTime.of(11, 0), false),
                Arguments.of(LocalTime.of(23, 0), true),
                Arguments.of(LocalTime.of(12, 0), false)
        );
    }

    public static Stream<Arguments> minusWHenNotMidNight() {
        return Stream.of(
                Arguments.of(LocalTime.of(11, 0), 60L),
                Arguments.of(LocalTime.of(6, 0), 360L)
        );
    }

    public static Stream<Arguments> minusWHenMidNight() {
        return Stream.of(
                Arguments.of(LocalTime.of(11, 0), 13 * 60L),
                Arguments.of(LocalTime.of(6, 0), 18 * 60L)
        );
    }

    @ParameterizedTest
    @MethodSource("localDate")
    void 시간_단위로_나눠지지_않는_시간일_경우_예외를_던진다(LocalTime localTime) {
        assertThatThrownBy(() -> AppointmentTime.of(localTime, 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR);
    }

    @ParameterizedTest
    @MethodSource("isMidNight")
    void 자정인지_확인한다(LocalTime localTime, boolean expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(localTime, 30);

        // when
        boolean actual = appointmentTime.isMidNight();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("appointmentTime_isAfter")
    void 다른_AppointmentTime_보다_이후인지_확인한다(LocalTime localTime, boolean expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(LocalTime.of(12,0), 30);
        AppointmentTime other = AppointmentTime.of(localTime, 30);

        // when
        boolean actual = appointmentTime.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    @ParameterizedTest
    @MethodSource("localTime_isAfter")
    void 다른_localTime보다_이후인지_확인한다(LocalTime localTime, boolean expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(LocalTime.of(12,0), 30);

        // when
        boolean actual = appointmentTime.isAfter(localTime);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("localTime_isBefore")
    void 다른_localTime보다_이전인지_확인한다(LocalTime localTime, boolean expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(LocalTime.of(12,0), 30);

        // when
        boolean actual = appointmentTime.isBefore(localTime);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("minusWHenNotMidNight")
    void 자정이_아닐때_분_차이를_계산한다(LocalTime localTime, long expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(LocalTime.of(12,0), 30);

        // when
        long actual = appointmentTime.minus(AppointmentTime.of(localTime, 30));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("minusWHenMidNight")
    void 자정일_때_분_차이를_계산한다(LocalTime localTime, long expected) {
        // given
        AppointmentTime appointmentTime = AppointmentTime.of(LocalTime.of(0,0), 30);

        // when
        long actual = appointmentTime.minus(AppointmentTime.of(localTime, 30));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
