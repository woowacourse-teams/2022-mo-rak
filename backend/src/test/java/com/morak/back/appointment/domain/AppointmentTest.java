package com.morak.back.appointment.domain;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusDays(1));
    }

    @Test
    void POJO객체의_count는_항상_0이다() {
        // when
        Appointment appointment = DEFAULT_BUILDER.build();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기_생성시_마지막_날짜와_시간이_현재보다_과거이면_예외를_던진다() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2L);
        LocalDate endDate = LocalDate.now().minusDays(1L);

        // then & when
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(LocalTime.of(0, 0))
                        .endTime(LocalTime.of(1, 30))
                        .durationHours(1)
                        .durationMinutes(0)
                        .closedAt(LocalDateTime.now().plusDays(1))
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
                        .closedAt(LocalDateTime.now().plusDays(1))
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR);
    }

    @Test
    void 마감시간이_마지막날짜보다_빠를수있다() {
        // given
        LocalDateTime closedAt = LocalDateTime.now().plusDays(5);
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);

        // when & then
        assertThatNoException().isThrownBy(
                () -> DEFAULT_BUILDER.closedAt(closedAt).
                        startDate(startDate)
                        .endDate(endDate)
                        .build()
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 11L, 15L})
    void 마감시간이_오늘과_마지막의_날짜를_벗어나면_예외를_던진다(long plusDays) {
        // given
        LocalDateTime closedAt = LocalDateTime.now().plusDays(plusDays);
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);

        // when & then
        assertThatThrownBy(() -> DEFAULT_BUILDER.closedAt(closedAt).
                startDate(startDate)
                .endDate(endDate)
                .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "0, -1",
            "10, 1111" // 18:30 + 1
    })
    void 마감시간이_오늘과_마지막의_시간을_벗어나면_예외를_던진다(long plusDays, long plusMinutes) {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime closedAt = LocalDateTime.now().plusDays(plusDays).plusMinutes(plusMinutes);

        // when & then
        assertThatThrownBy(() -> DEFAULT_BUILDER.closedAt(closedAt).
                startDate(startDate)
                .endDate(endDate)
                .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
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
        Member eden = Member.builder()
                .id(1L)
                .build();
        Appointment appointment = DEFAULT_BUILDER
                .host(eden)
                .build();

        // when
        appointment.close(eden);

        // then
        assertThat(appointment.getStatus()).isEqualTo(MenuStatus.CLOSED);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닐_경우_예외를_반환한다() {
        // given
        Member eden = Member.builder()
                .id(1L)
                .build();
        Appointment appointment = DEFAULT_BUILDER
                .host(eden)
                .build();

        Member ellie = Member.builder()
                .id(2L)
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.close(ellie))
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

    @Test
    @Disabled // to do : fix this
    void 약속잡기의_기간이_포함되는지_확인한다() {
        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//        DatePeriod datePeriod = new DatePeriod(
//                LocalDate.now().plusDays(1),
//                LocalDate.now().plusDays(3)
//        );

//        // when
//        boolean isAvailable = appointment.isAvailableDateRange(datePeriod);
//
//        // then
//        assertThat(isAvailable).isTrue();
    }

    @Test
    @Disabled // todo : fix this
    void 약속잡기의_기간이_포함되지_않으면_False를_반환한다() {
        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//        DatePeriod datePeriod = new DatePeriod(
//                LocalDate.now().plusDays(5),
//                LocalDate.now().plusDays(6)
//        );
//
//        // when
//        boolean isAvailable = appointment.isAvailableDateRange(datePeriod);
//
//        // then
//        assertThat(isAvailable).isFalse();
    }

    @Test
    @Disabled // todo : fix this
    void 약속잡기의_시간이_포함되는지_확인한다() {
//        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//        TimePeriod timePeriod = new TimePeriod(LocalTime.of(15, 0), LocalTime.of(15, 30));
//
//        // when
//        boolean isAvailable = appointment.isAvailableTimeRange(timePeriod);
//
//        // then
//        assertThat(isAvailable).isTrue();
    }

    @Test
    @Disabled // todo : fix this
    void 약속잡기의_시간이_포함되지_않으면_False를_반환한다() {
//        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//        TimePeriod timePeriod = new TimePeriod(LocalTime.of(18, 30), LocalTime.of(19, 0));
//
//        // when
//        boolean isAvailable = appointment.isAvailableTimeRange(timePeriod);
//
//        // then
//        assertThat(isAvailable).isFalse();
    }

    @Test
    void test() {
        /*
                .startDate(LocalDate.now().plusDays(1))
        .endDate(LocalDate.now().plusDays(5))
        .startTime(LocalTime.of(14, 0))
        .endTime(LocalTime.of(18, 30))
        .durationHours(1)
         */
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        // when
        List<AppointmentTime> appointmentTimes = appointment.getAppointmentTimes();
        // then
        System.out.println("appointmentTimes = " + appointmentTimes);
    }
}
