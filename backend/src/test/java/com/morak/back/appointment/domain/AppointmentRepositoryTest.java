package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Team team;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L).orElseThrow();
        team = teamRepository.findByCode("MoraK123").orElseThrow();
    }

    @Test
    void 약속잡기를_저장한다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // then
        assertThat(savedAppointment.getId()).isNotNull();
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();
        appointmentRepository.save(appointment);

        // when
        List<Appointment> appointments = appointmentRepository.findAllByTeamId(team.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(appointments).hasSize(2),
                () -> assertThat(appointments.get(1).getTitle()).isEqualTo("스터디 회의 날짜 정하기")
        );
    }

    @Test
    void 약속잡기_단건을_조회한다() {
        //given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        //when
        Appointment foundAppointment = appointmentRepository.findByCode(savedAppointment.getCode()).orElseThrow();

        //then
        assertThat(foundAppointment.getTitle()).isEqualTo("스터디 회의 날짜 정하기");
    }

    @Test
    void 약속잡기_제목이_빈값일_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 약속잡기_제목이_255자가_넘는_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("a".repeat(256))
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 약속잡기_설명이_1000자가_넘는_경우_예외를_던진다() {
        // given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description(null)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    // TODO: 2022/07/28 AvailableTime 추가 후 테스트 필요!!
    @Test
    void 포뮬라를_적용해_count를_불러온다() {
        // when
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        // then
        assertThat(appointment.getCount()).isEqualTo(0);
    }

    @Test
    void 약속잡기를_삭제한다() {
        //given
        Appointment appointment = Appointment.builder()
                .host(member)
                .team(team)
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        appointmentRepository.deleteById(savedAppointment.getId());

        //when
        Optional<Appointment> appointmentOptional = appointmentRepository.findByCode(savedAppointment.getCode());

        //then
        assertThat(appointmentOptional).isEmpty();
    }
}
