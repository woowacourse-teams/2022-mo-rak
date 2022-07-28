package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.builder;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class AvailableTimeTest {

    @Test
    void 약속잡기_가능_시간이_지정된_시간을_벗어날_경우_예외를_던진다() {
        // given
        Appointment appointment = builder()
                .host(new Member())
                .team(new Team())
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
        ).isInstanceOf(InvalidRequestException.class);
    }
}
