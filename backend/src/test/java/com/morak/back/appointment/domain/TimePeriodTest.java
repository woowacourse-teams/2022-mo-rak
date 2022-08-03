package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimePeriodTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_시작_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> TimePeriod.of(LocalTime.of(10, minutes), LocalTime.of(14, 0), 30))
                .isInstanceOf(InvalidRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_마지막_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> TimePeriod.of(LocalTime.of(10, 30), LocalTime.of(14, minutes), 30))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 약속잡기_마지막_시간이_자정일_경우_생성시_시간순_검증을_하지_않는다() {
        // given
        LocalTime midnight = LocalTime.of(0, 0);

        // when & then
        assertThatNoException().isThrownBy(
                () -> TimePeriod.of(LocalTime.of(0, 0), midnight, 30)
        );
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_23시_30분부터_24시이면_예외를_던진다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(10, 0), LocalTime.of(20, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(23, 30), LocalTime.of(0, 0), 30);

        // then
        assertThatThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_24시_0분부터_24시_30분이면_예외를_던진다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(10, 0), LocalTime.of(20, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(0, 0), LocalTime.of(0, 30), 30);

        // then
        assertThatThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_19시_0분부터_19시_30분이면_통과한다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(10, 0), LocalTime.of(20, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(19, 0), LocalTime.of(19, 30), 30);

        // then
        assertThatNoException().isThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod));
    }

    @Test
    void 약속잡기_가능시간이_10시부터_24시이고_선택시간이_23시_30분부터_24시이면_통과한다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(10, 0), LocalTime.of(0, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(23, 30), LocalTime.of(0, 0), 30);

        // then
        assertThatNoException().isThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod));
    }

    @Test
    void 약속잡기_가능시간이_10시부터_24시이고_선택시간이_24시_0분부터_24시_30분이면_예외를_던진다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(10, 0), LocalTime.of(0, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(0, 0), LocalTime.of(0, 30), 30);

        // then
        assertThatThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 약속잡기_가능시간이_0시부터_24시이고_선택시간이_23시_30분부터_24시이면_통과한다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(0, 0), LocalTime.of(0, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(23, 30), LocalTime.of(0, 0), 30);

        // then
        assertThatNoException().isThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod));
    }

    @Test
    void 약속잡기_가능시간이_0시부터_24시이고_선택시간이_24시_0분부터_24시_30분이면_통과한다() {
        // given
        TimePeriod appointmentTimePeriod = TimePeriod.of(LocalTime.of(0, 0), LocalTime.of(0, 0), 30);

        // when
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(0, 0), LocalTime.of(0, 30), 30);

        // then
        assertThatNoException().isThrownBy(() -> appointmentTimePeriod.validateAvailableTimeRange(timePeriod));
    }
}
