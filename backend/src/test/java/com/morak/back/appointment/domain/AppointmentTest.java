package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.menu.MenuStatus;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    private static AppointmentBuilder DEFAULT_BUILDER;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();

        now = LocalDateTime.now();

        DEFAULT_BUILDER = Appointment.builder()
                .hostId(1L)
                .teamCode("TEAMcode")
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .code(Code.generate(length -> "MoraK123"))
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .now(now)
                .closedAt(now.plusDays(1));
    }

    @Test
    void 사용자가_시간을_선택하지않았다면_선택_인원은_0이다() {
        // when
        Appointment appointment = DEFAULT_BUILDER.build();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간보다_짧으면_예외를_던진다() {
        // when & then
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .closedAt(now.plusDays(1))
                        .durationHours(5)
                        .durationMinutes(0)
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR);
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간과_같으면_예외를_던지지_않는다() {
        // then & when
        assertThatNoException().isThrownBy(
                () -> DEFAULT_BUILDER
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(11, 0))
                        .durationHours(1)
                        .durationMinutes(0)
                        .closedAt(LocalDateTime.now().plusDays(1))
                        .build()
        );
    }

    @Test
    void 약속잡기를_마감한다() {
        // given
        long hostId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .hostId(hostId)
                .build();

        // when
        appointment.close(hostId);

        // then
        assertThat(appointment.getStatus()).isEqualTo(MenuStatus.CLOSED);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닐_경우_예외를_반환한다() {
        // given
        long hostId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .hostId(hostId)
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.close(2L))
                .isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void durationMinutes객체의_시간의_시를_얻어온다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when
        Integer hours = appointment.parseHours();

        // then
        assertThat(hours).isEqualTo(1);
    }

    @Test
    void durationMinutes객체의_시간의_분을_얻어온다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when
        Integer minutes = appointment.parseMinutes();

        // then
        assertThat(minutes).isEqualTo(0);
    }
}
