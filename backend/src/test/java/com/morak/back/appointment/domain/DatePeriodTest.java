package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DatePeriodTest {

    @Test
    void 시작_날짜가_현재보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> DatePeriod.of(
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1),
                LocalTime.of(20, 0)
        )).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 마지막_날짜가_시작_날짜보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> DatePeriod.of(
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1),
                LocalTime.of(0, 0)
        )).isInstanceOf(InvalidRequestException.class);

    }

    @Test
    void 시작_날짜와_마지막_날짜는_같을_수_있다() {
        // when & then
        assertThatNoException()
                .isThrownBy(() -> DatePeriod.of(
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(2),
                        LocalTime.of(20, 0)));
    }

    @Test
    void 약속잡기_마지막_시간이_자정이면_하루를_추가한다() {
        // given
        LocalTime 자정 = LocalTime.of(0, 0);

        // when
        DatePeriod datePeriod = DatePeriod.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 자정);

        // then
        assertThat(datePeriod.getEndDate()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @Test
    void 약속잡기_시작_날짜와_마지막_날짜가_같고_마지막_시간이_자정일_경우_시작_날짜와_마지막_날짜가_달라진다() {
        // given
        LocalTime 자정 = LocalTime.of(0, 0);

        // when
        DatePeriod datePeriod = DatePeriod.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), 자정);

        // then
        Assertions.assertAll(
                () -> assertThat(datePeriod.getStartDate()).isNotEqualTo(datePeriod.getEndDate()),
                () -> assertThat(datePeriod.getEndDate()).isEqualTo(LocalDate.now().plusDays(2))
        );
    }
}
