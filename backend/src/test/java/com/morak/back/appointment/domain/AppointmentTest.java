package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.AppointmentStatus.CLOSED;
import static java.time.LocalTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
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
        Appointment appointment = Appointment.builder()
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
    void 약속잡기_생성시_마지막_날짜와_시간이_현재보다_과거이면_예외를_던진다() {

        // then & when
        assertThatThrownBy(
                () -> Appointment.builder()
                        .host(new Member())
                        .team(new Team())
                        .title("스터디 회의 날짜 정하기")
                        .description("필참!!")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .startTime(of(0, 0))
                        .endTime(of(1, 30))
                        .durationHours(1)
                        .durationMinutes(0)
                        .build()
        ).isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("약속잡기의 마지막 날짜와 시간은 현재보다 과거일 수 없습니다.");
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간보다_짧으면_예외를_던진다() {

        // then & when
        assertThatThrownBy(
                () -> Appointment.builder()
                        .host(new Member())
                        .team(new Team())
                        .title("스터디 회의 날짜 정하기")
                        .description("필참!!")
                        .startDate(LocalDate.now().plusDays(1))
                        .endDate(LocalDate.now().plusDays(5))
                        .startTime(of(10, 0))
                        .endTime(of(11, 0))
                        .durationHours(2)
                        .durationMinutes(0)
                        .build()
        ).isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("진행 시간은 약속잡기 시간보다 짧을 수 없습니다.");
    }

    @Test
    void 약속잡기_생성시_약속잡기_시간이_진행_시간과_같으면_예외를_던지지_않는다() {

        // then & when
        assertThatNoException().isThrownBy(
                        () -> Appointment.builder()
                                .host(new Member())
                                .team(new Team())
                                .title("스터디 회의 날짜 정하기")
                                .description("필참!!")
                                .startDate(LocalDate.now().plusDays(1))
                                .endDate(LocalDate.now().plusDays(5))
                                .startTime(of(10, 0))
                                .endTime(of(11, 0))
                                .durationHours(1)
                                .durationMinutes(0)
                                .build()
                );
    }

    @Test
    void 약속잡기를_마감한다() {
        //given
        Member eden = Member.builder().id(1L).build();
        Appointment appointment = Appointment.builder()
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
        Member eden = Member.builder().id(1L).build();
        Appointment appointment = Appointment.builder()
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
        Member ellie = Member.builder().id(2L).build();

        //when & then
        assertThatThrownBy(() -> appointment.close(ellie))
                .isInstanceOf(InvalidRequestException.class);
    }
}
