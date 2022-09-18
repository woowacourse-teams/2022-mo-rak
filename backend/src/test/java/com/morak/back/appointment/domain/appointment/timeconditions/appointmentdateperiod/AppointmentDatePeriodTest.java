package com.morak.back.appointment.domain.appointment.timeconditions.appointmentdateperiod;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.appointment.timeconditions.appointmenttimeperiod.AppointmentTime;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.core.domain.times.Times;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppointmentDatePeriodTest {
    private final Times times = new LocalTimes();

    public static Stream<Arguments> otherAppointmentDatePeriods() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(3);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(12, 0), 30);

        return Stream.of(
                Arguments.of(AppointmentDatePeriod.of(startDate, endDate, endTime, now), true),
                Arguments.of(AppointmentDatePeriod.of(startDate.plusDays(1), endDate.minusDays(1), endTime, now), true),
                Arguments.of(AppointmentDatePeriod.of(startDate.plusDays(1), endDate, endTime, now), true),
                Arguments.of(AppointmentDatePeriod.of(startDate, endDate.minusDays(1), endTime, now), true),
                Arguments.of(AppointmentDatePeriod.of(startDate.minusDays(1), endDate, endTime, now), false),
                Arguments.of(AppointmentDatePeriod.of(startDate, endDate.plusDays(1), endTime, now), false)
        );
    }

    @Test
    void 생성시_마지막_날짜가_시작_날짜보다_과거일_경우_예외를_던진다() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(1);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(20, 0), 30);

        // when & then
        assertThatThrownBy(() -> AppointmentDatePeriod.of(startDate, endDate, endTime, times.dateOfNow()))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR);
    }

    @Test
    void 생성시_시작_날짜와_마지막_날짜는_같을_수_있다() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(2);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(20, 0), 30);

        // when & then
        assertThatCode(() -> AppointmentDatePeriod.of(startDate, endDate, endTime, times.dateOfNow()))
                .doesNotThrowAnyException();
    }

    @Test
    void AppointmentDatePeriod를_생성시_약속잡기_마지막_시간이_자정이면_하루를_추가한다() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(2);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(0, 0), 30);

        // when
        AppointmentDatePeriod appointmentDatePeriod = AppointmentDatePeriod.of(startDate, endDate, endTime, now);

        // then
        assertThat(appointmentDatePeriod.getEndDate().getDate()).isEqualTo(now.plusDays(3));
    }

    @Test
    void 생성시_약속잡기_시작_날짜와_마지막_날짜가_같고_마지막_시간이_자정일_경우_시작_날짜와_마지막_날짜가_달라진다() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(1);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(0, 0), 30);

        // when
        AppointmentDatePeriod appointmentDatePeriod = AppointmentDatePeriod.of(startDate, endDate, endTime, now);

        // then
        assertThat(appointmentDatePeriod.getEndDate().getDate()).isEqualTo(now.plusDays(2));
    }

    @Test
    void 시간을_받으면_endDate와_합쳐_LocalDateTime으로_변환할_수_있다() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(1);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(12, 0), 30);

        AppointmentDatePeriod appointmentDatePeriod = AppointmentDatePeriod.of(startDate, endDate, endTime, now);

        // when
        LocalDateTime result = appointmentDatePeriod.toEndDateTime(endTime);

        // then
        assertThat(result).isEqualTo(LocalDateTime.of(endDate, endTime.getTime()));
    }

    @ParameterizedTest
    @MethodSource("otherAppointmentDatePeriods")
    void 다른_날짜_기간을_포함하는_지_확인한다(AppointmentDatePeriod other, boolean expected) {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(3);
        AppointmentTime endTime = AppointmentTime.of(LocalTime.of(12, 0), 30);
        AppointmentDatePeriod appointmentDatePeriod = AppointmentDatePeriod.of(startDate, endDate, endTime, now);

        // when
        boolean actual = appointmentDatePeriod.contains(other);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
