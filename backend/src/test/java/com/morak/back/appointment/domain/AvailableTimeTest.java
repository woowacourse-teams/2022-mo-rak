package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AvailableTimeTest {

    @Test
    void 약속잡기_선택의_날짜가_지정된_날짜를_벗어날_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .teamCode(Code.generate((length) -> "testTeam"))
                .code(Code.generate(length -> "ABCD1234"))
                .title("스터디 회의 날짜 정하기")
                .subTitle("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(1)
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusDays(1))
                .now(LocalDateTime.now())
                .build();

        // when & then

        assertThatThrownBy(() ->
                appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(14, 0))),
                        1L,
                        LocalDateTime.now()
                )
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 약속잡기_선택의_시간이_지정된_시간을_벗어날_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .teamCode(Code.generate((length) -> "testTeam"))
                .code(Code.generate(length -> "ABCD1234"))
                .title("스터디 회의 날짜 정하기")
                .subTitle("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(1)
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusDays(1))
                .now(LocalDateTime.now())
                .build();

        // when & then
        assertThatThrownBy(() -> appointment.selectAvailableTime(
                        Set.of(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 0))),
                        1L,
                        LocalDateTime.now()
                )
        ).isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 선택한_멤버가_맞는지_확인한다() {
        // given
        long memberId = 1L;
        AvailableTime availableTime = AvailableTime.builder().startDateTime(LocalDateTime.now()).memberId(memberId).build();

        // when
        boolean actual = availableTime.matchMember(memberId);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 선택한_멤버가_아닌지_확인한다() {
        // given
        long memberId = 1L;
        AvailableTime availableTime = AvailableTime.builder().startDateTime(LocalDateTime.now()).memberId(memberId).build();

        // when
        boolean actual = availableTime.matchMember(memberId + 1L);

        // then
        assertThat(actual).isFalse();
    }
}
