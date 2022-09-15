package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.appointment.AppointmentStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.appointment.domain.appointment.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.availabletime.datetimeperiod.AvailableTimeDateTimePeriod;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
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
                .times(new LocalTimes());
    }

    @Test
    void POJO객체의_count는_항상_0이다() {
        // when
        Appointment appointment = 정상적인_약속_생성();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기_생성시_마지막_날짜와_시간이_현재보다_과거이면_예외를_던진다() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(18, 30);
        int durationHours = 1;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() ->
                DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_PAST_DATE_CREATE_ERROR);
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간보다_짧으면_예외를_던진다() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2L);
        LocalDate endDate = LocalDate.now().minusDays(1L);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int durationHours = 2;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR);
    }

    @Test
    void 마감시간이_마지막날짜보다_빠를수있다() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int durationHours = 1;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(5);

        // when & then
        assertThatCode(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
                        .build()
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 11L, 15L})
    void 마감시간이_오늘과_마지막의_날짜를_벗어나면_예외를_던진다(long plusDays) {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int durationHours = 1;
        int durationMinutes = 0;

        LocalDateTime closedAt = LocalDateTime.now().plusDays(plusDays);

        // when & then
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
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
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(18, 30);
        int durationHours = 1;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(plusDays).plusMinutes(plusMinutes);

        // when & then
        assertThatThrownBy(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
                        .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간과_같으면_예외를_던지지_않는다() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int durationHours = 1;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(5);

        // when & then
        assertThatCode(
                () -> DEFAULT_BUILDER
                        .startDate(startDate)
                        .endDate(endDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .durationHours(durationHours)
                        .durationMinutes(durationMinutes)
                        .closedAt(closedAt)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void 약속잡기를_마감한다() {
        // given
        Appointment appointment = 정상적인_약속_생성();

        // when
        appointment.close(appointment.getHost());

        // then
        assertThat(appointment.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닐_경우_예외를_반환한다() {
        // given
        Appointment appointment = 정상적인_약속_생성();

        // when & then
        assertThatThrownBy(() -> appointment.close(new Member()))
                .isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void durationMinutes객체의_시간의_시를_얻어온다() {
        // given
        Appointment appointment = 정상적인_약속_생성();

        // when
        Integer hours = appointment.parseDurationHours();

        // then
        assertThat(hours).isEqualTo(1);
    }

    @Test
    void durationMinutes객체의_시간의_분을_얻어온다() {
        // given
        Appointment appointment = 정상적인_약속_생성();

        // when
        Integer minutes = appointment.parseDurationMinutes();

        // then
        assertThat(minutes).isEqualTo(0);
    }

    @Test
    void 약속잡기의_기간이_포함되는지_확인한다() {
        // given
        Appointment appointment = 정상적인_약속_생성();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        AvailableTimeDateTimePeriod availableTimeDateTimePeriod =
                AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, LocalDateTime.now());

        // when & then
        assertThatCode(() -> appointment.validateDateTimePeriod(availableTimeDateTimePeriod))
                .doesNotThrowAnyException();
    }

    @Test
    void 약속잡기의_기간이_포함되지_않으면_예외를_던진다() {
        // given
        Appointment appointment = 정상적인_약속_생성();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        AvailableTimeDateTimePeriod availableTimeDateTimePeriod =
                AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> appointment.validateDateTimePeriod(availableTimeDateTimePeriod))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    private Appointment 정상적인_약속_생성() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int durationHours = 1;
        int durationMinutes = 0;
        LocalDateTime closedAt = LocalDateTime.now().plusDays(5);

        Member eden = Member.builder()
                .id(1L)
                .build();

        return DEFAULT_BUILDER
                .host(eden)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .durationHours(durationHours)
                .durationMinutes(durationMinutes)
                .closedAt(closedAt)
                .build();
    }
}
