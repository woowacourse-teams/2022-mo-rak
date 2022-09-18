package com.morak.back.appointment.domain.appointment.timeconditions.appointmentdateperiod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.core.domain.times.Times;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppointmentDateTest {
    private final Times times = new LocalTimes();


    @Test
    void 약속잡기_날짜_생성_테스트() {
        // given
        LocalDate appointmentDate = LocalDate.now().plusDays(1);
        LocalDate now = times.dateOfNow();

        // when & then
        assertThatCode(() -> AppointmentDate.of(appointmentDate, now))
                .doesNotThrowAnyException();
    }

    @Test
    void 약속잡기_날짜_생성_시_현재보다_과거면_예외를_던진다() {
        // given
        LocalDate appointmentDate = LocalDate.now().minusDays(1);
        LocalDate now = times.dateOfNow();

        // when & then
        assertThatThrownBy(() -> AppointmentDate.of(appointmentDate, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR);
    }

    @ParameterizedTest
    @MethodSource("localDates")
    void 다른_날짜와_비교하여_이전인지_확인할_수_있다(LocalDate other, boolean expected) {
        // given
        LocalDate date = LocalDate.now().plusDays(1);

        AppointmentDate appointmentDate = AppointmentDate.of(date, LocalDate.now());

        // when
        boolean actual = appointmentDate.isBefore(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> localDates() {
        return Stream.of(
                Arguments.of(LocalDate.now(), false),
                Arguments.of(LocalDate.now().plusDays(2), true)
        );
    }

    @ParameterizedTest
    @MethodSource("afterTestAppointments")
    void 다른_appointmentDate와_비교하여_이후인지_확인할_수_있다(AppointmentDate other, boolean expected) {
        // given
        LocalDate date = LocalDate.now().plusDays(1);

        AppointmentDate appointmentDate = AppointmentDate.of(date, LocalDate.now());

        // when
        boolean actual = appointmentDate.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> afterTestAppointments() {
        LocalDate now = LocalDate.now().minusDays(1);

        return Stream.of(
                Arguments.of(AppointmentDate.of(LocalDate.now(), now), true),
                Arguments.of(AppointmentDate.of(LocalDate.now().plusDays(2), now), false)
        );
    }


    @ParameterizedTest
    @MethodSource("afterTestLocalDates")
    void 다른_localDate와_비교하여_이후인지_확인할_수_있다(LocalDate other, boolean expected) {
        // given
        LocalDate date = LocalDate.now().plusDays(1);

        AppointmentDate appointmentDate = AppointmentDate.of(date, LocalDate.now());

        // when
        boolean actual = appointmentDate.isAfter(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> afterTestLocalDates() {
        return Stream.of(
                Arguments.of(LocalDate.now(), true),
                Arguments.of(LocalDate.now().plusDays(2), false)
        );
    }

    @Test
    void 날짜를_하루_더할_수_있다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);

        AppointmentDate appointmentDate = AppointmentDate.of(date, LocalDate.now());

        // when
        appointmentDate.plusDays(1);

        // then
        assertThat(appointmentDate.getDate()).isEqualTo(date.plusDays(1));
    }
}
