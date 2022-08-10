package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class AvailableTimeTest {

    @Test
    void 약속잡기_선택의_날짜가_지정된_날짜를_벗어날_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(new Member())
                .team(new Team())
                .code(Code.generate(length -> "ABCD1234"))
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when & then
        assertThatThrownBy(() -> AvailableTime.builder()
                .id(1L)
                .appointment(appointment)
                .member(new Member())
                .startDateTime(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(14, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(14, 30)))
                .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 약속잡기_선택의_시간이_지정된_시간을_벗어날_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(new Member())
                .team(new Team())
                .code(Code.generate(length -> "ABCD1234"))
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when & then
        assertThatThrownBy(() -> AvailableTime.builder()
                .id(1L)
                .appointment(appointment)
                .member(new Member())
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 30)))
                .build()
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR);
    }
}
