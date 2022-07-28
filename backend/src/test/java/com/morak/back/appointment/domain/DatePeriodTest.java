package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DatePeriodTest {

    @Test
    void 시작_날짜가_현재보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new DatePeriod(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 마지막_날짜가_시작_날짜보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new DatePeriod(LocalDate.now().plusDays(2), LocalDate.now().plusDays(1)))
                .isInstanceOf(InvalidRequestException.class);

    }

    @Test
    void 시작_날짜와_마지막_날짜는_같을_수_있다() {
        // when & then
        assertThatNoException()
                .isThrownBy(() -> new DatePeriod(LocalDate.now().plusDays(2), LocalDate.now().plusDays(2)));
    }
}
