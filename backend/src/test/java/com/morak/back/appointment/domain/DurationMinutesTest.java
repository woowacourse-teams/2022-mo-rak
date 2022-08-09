package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DurationMinutesTest {

    @ParameterizedTest
    @CsvSource({
            "1, 0, 60",
            "2, 30, 150"
    })
    void 약속잡기_시간과_분을_합친다(int hours, int minutes, int total) {
        // given
        DurationMinutes durationMinutes = DurationMinutes.of(hours, minutes, 30);

        // when & then
        assertThat(durationMinutes.getDurationMinutes()).isEqualTo(total);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "2, 30"
    })
    void 약속잡기_진행_시간에서_시간과_분을_추출한다(int hours, int minutes) {
        // given
        DurationMinutes durationMinutes = DurationMinutes.of(hours, minutes, 30);

        // when & then
        assertAll(
                () -> assertThat(durationMinutes.parseHours()).isEqualTo(hours),
                () -> assertThat(durationMinutes.parseMinutes()).isEqualTo(minutes)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 25})
    void 약속잡기_진행_시간이_자연적이지_않을_경우_예외를_던진다(int hours) {
        // when & then
        assertThatThrownBy(() -> DurationMinutes.of(hours, 0, 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 60})
    void 약속잡기_진행_시간의_분이_자연적이지_않을_경우_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> DurationMinutes.of(0, minutes, 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_진행_시간이_30분_단위가_아닐_경우_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> DurationMinutes.of(0, minutes, 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "24, 30"
    })
    void 약속잡기_진행_시간이_범위를_벗어날_경우_예외를_던진다(int hours, int minutes) {
        // when & then
        assertThatThrownBy(() -> DurationMinutes.of(hours, minutes, 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }
}
