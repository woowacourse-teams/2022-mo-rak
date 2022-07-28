package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.builder;
import static com.morak.back.appointment.domain.AppointmentStatus.CLOSED;
import static java.time.LocalTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    // TODO: 2022/07/28 AvailableTime 추가 후 테스트 필요!!
    @Test
    void 포뮬라를_적용해_count를_불러온다() {
        // when
        Appointment appointment = builder()
                .host(new Member())
                .team(new Team())
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(of(14, 0))
                .endTime(of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기를_마감한다() {
        //given
        Member eden = new Member(1L, "oauth", "eden", "eden-profile.com");
        Appointment appointment = builder()
                .host(eden)
                .team(new Team())
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(of(14, 0))
                .endTime(of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        //when
        appointment.close(eden);

        //then
        assertThat(appointment.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닐_경우_예외를_반환한다() {
        //given
        Member eden = new Member(1L, "oauth", "eden", "eden-profile.com");
        Appointment appointment = builder()
                .host(eden)
                .team(new Team())
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(of(14, 0))
                .endTime(of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();
        Member ellie = new Member(2L, "oauthId2", "ellie", " ellie - profile.com");

        //when & then
        assertThatThrownBy(() -> appointment.close(ellie))
                .isInstanceOf(InvalidRequestException.class);
    }
}
