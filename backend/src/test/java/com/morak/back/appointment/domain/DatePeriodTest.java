package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DatePeriodTest {

    private final LocalDate now = LocalDate.now();


    @Test
    void 생성시_시작_날짜가_현재보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> {
            new DatePeriod(
                    now.minusDays(2),
                    now.minusDays(1),
                    now
            );
        }).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR);
    }

    @Test
    void 생성시_마지막_날짜가_시작_날짜보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new DatePeriod(
                now.plusDays(2),
                now.plusDays(1),
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR);

    }

    @Test
    void 생성시_시작_날짜와_마지막_날짜는_같을_수_있다() {
        // when & then
        LocalDate now = LocalDate.now();
        assertThatNoException()
                .isThrownBy(() -> new DatePeriod(
                        now.plusDays(2),
                        now.plusDays(2),
                        now
                ));
    }

    @Test
    void 생성시_약속잡기_마지막_시간이_자정이면_하루를_추가한다() {
        // given
        LocalTime 자정 = LocalTime.of(0, 0);

        // when
        DatePeriod datePeriod = new DatePeriod(now.plusDays(1), now.plusDays(2), now);

        // then
        assertThat(datePeriod.getEndDate()).isEqualTo(now.plusDays(3));
    }

    @Test
    @Disabled // todo : delete this
    void 생성시_약속잡기_시작_날짜와_마지막_날짜가_같고_마지막_시간이_자정일_경우_시작_날짜와_마지막_날짜가_달라진다() {
        // given
        LocalTime 자정 = LocalTime.of(0, 0);

        // when
        DatePeriod datePeriod = new DatePeriod(now.plusDays(1), now.plusDays(1), now);

        // then
        Assertions.assertAll(
                () -> assertThat(datePeriod.getStartDate()).isNotEqualTo(datePeriod.getEndDate()),
                () -> assertThat(datePeriod.getEndDate()).isEqualTo(now.plusDays(2))
        );
    }
}
