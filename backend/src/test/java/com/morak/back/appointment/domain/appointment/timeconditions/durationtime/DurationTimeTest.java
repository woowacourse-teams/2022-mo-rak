package com.morak.back.appointment.domain.appointment.timeconditions.durationtime;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DURATION_HOUR_OUT_OF_RANGE_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DURATION_MINUTE_OUT_OF_RANGE_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DURATION_MINUTE_RANGE_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DurationTimeTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 25})
    void 시간_범위에_들어가지_않는_경우_예외를_던진다(int hours) {
        assertThatThrownBy(() -> DurationTime.of(hours, 0, 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DURATION_HOUR_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 60})
    void 분_범위에_들어가지_않는_경우_예외를_던진다(int minutes) {
        assertThatThrownBy(() -> DurationTime.of(1, minutes, 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DURATION_MINUTE_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 31, 59})
    void 분_단위에_안_나눠_떨어지는_경우_예외_던진다(int minutes) {
        // given
        int minuteUnit = 30;

        // when
        assertThatThrownBy(() -> DurationTime.of(1, minutes, minuteUnit))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,0", "24,30"})
    void 시간과_분을_합쳤을_때_범위에_어긋나는_경우_예외를_던진다(int hours, int minutes) {
        assertThatThrownBy(() -> DurationTime.of(hours, minutes, 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DURATION_MINUTE_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 23})
    void 시간_범위에_들어가는_경우_생성할_수_있다(int hours) {
        assertThatCode(() -> DurationTime.of(hours, 0, 30))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 30})
    void 분_범위에_들어가는_경우_생성할_수_있다(int minutes) {
        assertThatCode(() -> DurationTime.of(1, minutes, 30))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {30, 60})
    void 입력받은_minutes보다_큰_durtaionMinute를_가지고있는지_확인한다(long minutes) {
        // given
        DurationTime durationTime = DurationTime.of(1, 30, 30);

        // when
        boolean actual = durationTime.isBiggerThan(minutes);

        // then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {90, 120})
    void 입력받은_minutes보다_같거나_작은_durtaionMinute를_가지고있는지_확인한다(long minutes) {
        // given
        DurationTime durationTime = DurationTime.of(1, 30, 30);

        // when
        boolean actual = durationTime.isBiggerThan(minutes);

        // then
        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,30,1", "23,30,23"})
    void durationTime이_몇_시간인지_확인한다(int hours, int minutes, int expected) {
        // given
        DurationTime durationTime = DurationTime.of(hours, minutes, 30);

        // when
        int actual = durationTime.parseHours();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,30,30", "23,30,30"})
    void durationTime에서_시간을_제외하면_몇_분이_남는지_확인한다(int hours, int minutes, int expected) {
        // given
        DurationTime durationTime = DurationTime.of(hours, minutes, 30);

        // when
        int actual = durationTime.parseMinutes();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
