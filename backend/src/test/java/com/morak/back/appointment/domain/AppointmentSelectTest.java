package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class AppointmentSelectTest {

    private AppointmentBuilder DEFAULT_BUILDER;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2022, 10, 17, 12, 0, 0);
        LocalDate today = now.toLocalDate();

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
    void 약속잡기_가능시간을_선택할_때_약속잡기가_이미_마감되었으면_예외를_던진다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        appointment.close(1L);

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(16, 0))),
                1L, 
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"13, 30", "18, 30"})
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(3), LocalTime.of(hour, minute))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = DEFAULT_BUILDER.endTime(LocalTime.of(0, 0)).build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(16, 0))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"13, 30", "0, 0"})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER.endTime(LocalTime.of(0, 0)).build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(3), LocalTime.of(hour, minute))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(16, 0))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"1, 0, 0", "5, 23, 30"})
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(int plusDay, int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        // when & then
        assertThatNoException().isThrownBy(
                () -> appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(hour, minute))),
                        1L,
                        now
                )
        );
    }

    @ParameterizedTest
    @CsvSource({"0, 23, 30", "6, 0, 0"})
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(int plusDay, int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(hour, minute))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"1, 0, 0", "1, 23, 30"})
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(int plusDay, int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .startDate(now.toLocalDate().plusDays(1L))
                .endDate(now.toLocalDate().plusDays(1L))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        // when & then
        assertThatNoException().isThrownBy(
                () -> appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(hour, minute))),
                        1L,
                        now
                )
        );
    }

    @ParameterizedTest
    @CsvSource({"0, 23, 30", "2, 0, 0"})
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(
            int plusDay, int hour, int minute) {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .startDate(now.toLocalDate().plusDays(1L))
                .endDate(now.toLocalDate().plusDays(1L))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(hour, minute))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
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
        Appointment appointment = DEFAULT_BUILDER
                .startDate(now.toLocalDate().plusDays(1L))
                .endDate(now.toLocalDate().plusDays(1L))
                .startTime(LocalTime.of(23, 30))
                .endTime(LocalTime.of(0, 0))
                .durationHours(0)
                .durationMinutes(30)
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                Set.of(LocalDateTime.of(now.toLocalDate().plusDays(plusDay), LocalTime.of(hour, minute))),
                1L,
                now
        )).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }
}
