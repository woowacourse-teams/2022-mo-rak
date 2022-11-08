package com.morak.back.appointment.domain.dateperiod;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR;
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

class DateTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 100})
    void 현재_날짜보다_이전의_날짜의_객체는_생성할_수_없다(int minusDays) {
        // given
        LocalDate today = LocalDate.now();
        LocalDate invalidDate = today.minusDays(minusDays);

        // when & then
        assertThatThrownBy(() -> new Date(invalidDate, today))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_PAST_DATE_CREATE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 100})
    void 현재_날짜와_같거나_이후의_날짜의_객체는_생성할_수_있다(int plusDays) {
        // given
        LocalDate today = LocalDate.now();
        LocalDate validDate = today.plusDays(plusDays);

        // when & then
        assertThatNoException().isThrownBy(() -> new Date(validDate, today));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,true", "1,false", "2,false"})
    void 다른_날짜_객체보다_이후인지_확인한다(int days, boolean expected) {
        // given
        LocalDate today = LocalDate.now();
        Date date = new Date(today.plusDays(1), today);
        Date other = new Date(today.plusDays(days), today);

        // when
        boolean actual = date.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,true", "1,false", "2,false"})
    void 다른_날짜_보다_이후인지_확인한다(int days, boolean expected) {
        // given
        LocalDate today = LocalDate.now();
        Date date = new Date(today.plusDays(1), today);
        LocalDate otherDate = today.plusDays(days);

        // when
        boolean actual = date.isAfter(otherDate);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,false", "1,false", "2,true"})
    void 다른_날짜_보다_이전인지_확인한다(int days, boolean expected) {
        // given
        LocalDate today = LocalDate.now();
        Date date = new Date(today.plusDays(1), today);
        LocalDate otherDate = today.plusDays(days);

        // when
        boolean actual = date.isBefore(otherDate);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 현재날짜로부터_1년_이후의_날짜라면_예외를_던진다() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate invalidDate = today.plusYears(1).plusDays(1);

        // when & then
        assertThatThrownBy(() -> new Date(invalidDate, today))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DATE_AFTER_YEAR_ERROR);
    }

    @Test
    void 현재날짜로부터_1년뒤의_날짜까지는_생성_가능하다() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate invalidDate = today.plusYears(1);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> new Date(invalidDate, today));
    }
}
