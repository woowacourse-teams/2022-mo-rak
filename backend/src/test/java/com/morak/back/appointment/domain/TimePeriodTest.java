package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimePeriodTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_시작_시간이_30분_단위가_아닐_경우_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, minutes), LocalTime.of(14, 0)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_마지막_시간이_30분_단위가_아닐_경우_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, 30), LocalTime.of(14, minutes)))
                .isInstanceOf(InvalidRequestException.class);
    }
}
