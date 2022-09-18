package com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.appointment.timeconditions.durationtime.DurationTime;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class AppointmentTimePeriodTest {

    public static Stream<Arguments> containsWhenNotMidNight() {
        int minuteUnit = 30;
        return Stream.of(
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(20, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), true),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(13, 0), LocalTime.of(19, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), true),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(11, 0), LocalTime.of(20, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), false),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(21, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), false)
        );
    }

    public static Stream<Arguments> containsWhenMidNight() {
        int minuteUnit = 30;
        return Stream.of(
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(0, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), true),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(13, 0), LocalTime.of(19, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), true),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(11, 0), LocalTime.of(20, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), false),
                Arguments.of(AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(21, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit), true)
        );
    }

    @Test
    void 약속잡기_마지막_시간이_자정일_경우_생성시_시간순_검증을_하지_않는다() {
        // given
        int minuteUnit = 30;
        LocalTime midnight = LocalTime.of(0, 0);

        // when & then
        assertThatNoException().isThrownBy(
                () -> AppointmentTimePeriod.of(
                        LocalTime.of(0, 0), midnight,
                        DurationTime.of(1, 30, minuteUnit), minuteUnit
                )
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"2,1", "12,1", "13,12"})
    void 약속잡기_마지막_시간이_시작_시간보다_빠를경우_예외를_던진다(int startTimeHour, int endTimeHour) {
        // given
        int minuteUnit = 30;

        // when & then
        assertThatThrownBy(() -> AppointmentTimePeriod.of(
                        LocalTime.of(startTimeHour, 0), LocalTime.of(endTimeHour, 0),
                        DurationTime.of(1, 0, minuteUnit), minuteUnit
                )
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,0,1,0,1,30", "23,0,0,0,1,30", "0,0,0,30,1,30"})
    void 약속_진행_시간이_선택_시간보다_크면_예외를_던진다(int startHour, int startMinutes,
                                      int endHour, int endMinutes,
                                      int hours, int minutes) {
        // given
        int minuteUnit = 30;

        // when & then
        assertThatThrownBy(() -> AppointmentTimePeriod.of(
                        LocalTime.of(startHour, startMinutes), LocalTime.of(endHour, endMinutes),
                        DurationTime.of(hours, minutes, minuteUnit), minuteUnit
                )
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR);
    }


    @ParameterizedTest
    @CsvSource(value = {"0,0,0,0,1,30", "12,0,0,0,8,30", "0,0,0,30,0,30"})
    void 약속_진행_시간을_정상적으로_생성한다(int startHour, int startMinutes,
                              int endHour, int endMinutes,
                              int hours, int minutes) {
        // given
        int minuteUnit = 30;

        // when & then
        assertThatCode(() -> AppointmentTimePeriod.of(
                        LocalTime.of(startHour, startMinutes), LocalTime.of(endHour, endMinutes),
                        DurationTime.of(hours, minutes, minuteUnit), minuteUnit
                )
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("containsWhenNotMidNight")
    void 끝_시간이_자정이_아닌_약속_진행_시간에_포함되는_기간인지_확인한다(AppointmentTimePeriod other, boolean expected) {
        // given
        int minuteUnit = 30;
        AppointmentTimePeriod appointmentTimePeriod = AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(20, 0),
                DurationTime.of(1, 0, minuteUnit), minuteUnit);

        // when
        boolean actual = appointmentTimePeriod.contains(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("containsWhenMidNight")
    void 끝_시간이_자정인_경우_진행_시간에_포함되는_기간인지_확인한다(AppointmentTimePeriod other, boolean expected) {
        // given
        int minuteUnit = 30;
        AppointmentTimePeriod appointmentTimePeriod = AppointmentTimePeriod.of(LocalTime.of(12, 0), LocalTime.of(0, 0),
                DurationTime.of(1, 0, minuteUnit), minuteUnit);

        // when
        boolean actual = appointmentTimePeriod.contains(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
