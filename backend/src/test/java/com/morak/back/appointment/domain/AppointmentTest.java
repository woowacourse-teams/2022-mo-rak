package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.recommend.AppointmentTime;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AppointmentTest {

    private AppointmentBuilder DEFAULT_BUILDER;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();

        now = LocalDateTime.now();

        DEFAULT_BUILDER = Appointment.builder()
                .hostId(1L)
                .teamCode(Code.generate((length) -> "TEAMcode"))
                .title("스터디 회의 날짜 정하기")
                .subTitle("필참!!")
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
        assertThat(appointment.getStatus()).isEqualTo(MenuStatus.CLOSED.name());
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
                .isInstanceOf(AuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.HOST_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기의_약속시간_후보를_얻어온다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when
        List<AppointmentTime> appointmentTimes = appointment.getAppointmentTimes();

        // then
        assertThat(appointmentTimes).hasSize(5 * 8);
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
    void 약속잡기_가능시간을_선택할_때_약속잡기가_이미_마감되었으면_예외를_던진다() {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER.build();
        appointment.close(memberId);

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(1), LocalTime.of(16, 0))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER.build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(16, 0))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "13, 30",
            "20, 0"
    })
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER.build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(1), LocalTime.of(hour, minute))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(16, 0))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"13, 30", "0, 0"})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(5), LocalTime.of(hour, minute))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);

    }


    @ParameterizedTest
    @CsvSource({
            "1, 0, 0",
            "5, 23, 30"
    })
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(int plusDay, int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatNoException().isThrownBy(
                (() -> appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(today.plusDays(5), LocalTime.of(hour, minute))),
                        memberId,
                        now
                ))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30",
            "6, 0, 0"
    })
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(int plusDay, int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(hour, minute))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0",
            "1, 23, 30"
    })
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(int plusDay, int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .endDate(now.toLocalDate().plusDays(1))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatNoException().isThrownBy(
                () -> appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(hour, minute))),
                        memberId,
                        now
                ));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30",
            "2, 0, 0"
    })
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(int plusDay, int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .endDate(now.toLocalDate().plusDays(1))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(hour, minute))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30", // 전혀 다른날
            "1, 0, 0", // 같은날, 다른시간
            "1, 23, 0", // 시작 시간 경계
            "2, 0, 0" // 마지막 시간 경계
    })
    void 하루동안_삼십분짜리_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(int plusDay, int hour, int minute) {
        // given
        long memberId = 1L;
        Appointment appointment = DEFAULT_BUILDER
                .endDate(now.toLocalDate().plusDays(1))
                .startTime(LocalTime.of(23, 30))
                .endTime(LocalTime.of(0, 0))
                .durationHours(0)
                .durationMinutes(30)
                .build();

        LocalDate today = now.toLocalDate();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(today.plusDays(plusDay), LocalTime.of(hour, minute))),
                memberId,
                now
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }
}
