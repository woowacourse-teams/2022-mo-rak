package com.morak.back.appointment.domain.dateperiod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DatePeriodTest {

    private final LocalDate now = LocalDate.now();

    @Test
    void 생성시_마지막_날짜가_시작_날짜보다_과거일_경우_예외를_던진다() {
        // given
        LocalDate startDate = now.plusDays(2);
        LocalDate endDate = now.plusDays(1);

        // when & then
        assertThatThrownBy(() -> new DatePeriod(startDate, endDate, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR);

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void 생성시_시작_날짜와_마지막_날짜는_같거나_이후여야한다(int plusDays) {
        // when & then
        LocalDate now = LocalDate.now();
        LocalDate startDate = now;
        LocalDate endDate = now.plusDays(plusDays);

        assertThatNoException()
                .isThrownBy(() -> new DatePeriod(startDate, endDate, now));
    }

    @ParameterizedTest
    @CsvSource(value = {"-1,false", "0,true", "1,true", "2,true", "3,false"})
    void 특정_날짜가_날짜기간안에_속하는_지_확인한다(int plusDays, boolean expected) {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now;
        LocalDate endDate = now.plusDays(2);

        DatePeriod datePeriod = new DatePeriod(startDate, endDate, now);

        LocalDate otherDate = now.plusDays(plusDays);

        // when
        boolean actual = datePeriod.isBetween(otherDate);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
