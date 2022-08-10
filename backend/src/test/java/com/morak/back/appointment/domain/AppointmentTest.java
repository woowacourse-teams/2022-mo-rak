package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.AppointmentStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    private static AppointmentBuilder DEFAULT_BUILDER;

    @BeforeEach
    void setUp() {
         DEFAULT_BUILDER = Appointment.builder()
                .host(new Member())
                .team(new Team())
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .code(Code.generate(length -> "MoraK123"))
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0);
    }

    // TODO: 2022/07/28 AvailableTime 추가 후 테스트 필요!!
    @Test
    void POJO객체의_count는_항상_0이다() {
        // when
        Appointment appointment = DEFAULT_BUILDER.build();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기_생성시_마지막_날짜와_시간이_현재보다_과거이면_예외를_던진다() {
        // then & when
        // TODO : mocking
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .startTime(LocalTime.of(0, 0))
                        .endTime(LocalTime.of(1, 30))
                        .durationHours(1)
                        .durationMinutes(0)
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_PAST_CREATE_ERROR);
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간보다_짧으면_예외를_던진다() {
        // then & when
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(11, 0))
                        .durationHours(2)
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
                        .build()
        );
    }

    @Test
    void 약속잡기를_마감한다() {
        //given
        Member eden = Member.builder()
                .id(1L)
                .build();
        Appointment appointment = DEFAULT_BUILDER
                .host(eden)
                .build();

        //when
        appointment.close(eden);

        //then
        assertThat(appointment.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닐_경우_예외를_반환한다() {
        //given
        Member eden = Member.builder()
                .id(1L)
                .build();
        Appointment appointment = DEFAULT_BUILDER
                .host(eden)
                .build();

        Member ellie = Member.builder()
                .id(2L)
                .build();

        //when & then
        assertThatThrownBy(() -> appointment.close(ellie))
                .isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void durationMinutes객체의_시간의_시을_얻어온다() {
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

    @Test
    void 약속잡기의_기간이_포함되는지_확인한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        DatePeriod datePeriod = DatePeriod.of(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                LocalTime.of(16, 0)
        );

        // when
        boolean isAvailable = appointment.isAvailableDateRange(datePeriod);

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    void 약속잡기의_기간이_포함되지_않으면_False를_반환한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        DatePeriod datePeriod = DatePeriod.of(
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(6),
                LocalTime.of(16, 0)
        );

        // when
        boolean isAvailable = appointment.isAvailableDateRange(datePeriod);

        // then
        assertThat(isAvailable).isFalse();
    }

    @Test
    void 약속잡기의_시간이_포함되는지_확인한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(15, 0), LocalTime.of(15, 30));

        // when
        boolean isAvailable = appointment.isAvailableTimeRange(timePeriod);

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    void 약속잡기의_시간이_포함되지_않으면_False를_반환한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        TimePeriod timePeriod = TimePeriod.of(LocalTime.of(18, 30), LocalTime.of(19, 0));

        // when
        boolean isAvailable = appointment.isAvailableTimeRange(timePeriod);

        // then
        assertThat(isAvailable).isFalse();
    }
}
