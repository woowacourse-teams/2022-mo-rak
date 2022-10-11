package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class AppointmentRepositoryTest {

    private static AppointmentBuilder DEFAULT_BUILDER;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Team team;

    static List<Arguments> getAppointmentWithEachNull() {
        Function<AppointmentBuilder, AppointmentBuilder> hostNull =
                (AppointmentBuilder builder) -> builder.host(null);
        Function<AppointmentBuilder, AppointmentBuilder> teamNull =
                (AppointmentBuilder builder) -> builder.team(null);
        Function<AppointmentBuilder, AppointmentBuilder> titleNull =
                (AppointmentBuilder builder) -> builder.title(null);
        Function<AppointmentBuilder, AppointmentBuilder> descriptionNull =
                (AppointmentBuilder builder) -> builder.description(null);

        return List.of(
                Arguments.of(hostNull),
                Arguments.of(teamNull),
                Arguments.of(titleNull),
                Arguments.of(descriptionNull)
        );
    }

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .build();
        team = Team.builder()
                .id(1L)
                .build();
        DEFAULT_BUILDER = Appointment.builder()
                .title("스터디 회의 날짜 정하기")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusDays(1))
                .code(Code.generate(length -> "FJn3ND26"))
                .closedAt(LocalDateTime.now().plusDays(1))
                .host(member)
                .team(team);
    }

    @Test
    void 약속잡기를_저장한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // then
        assertThat(savedAppointment.getId()).isNotNull();
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        appointmentRepository.save(appointment);

        // when
        List<Appointment> appointments = appointmentRepository.findAllByMenuTeam(team);

        // then
        Assertions.assertAll(
                () -> assertThat(appointments).hasSize(2),
                () -> assertThat(appointments.get(1).getTitle()).isEqualTo("스터디 회의 날짜 정하기")
        );
    }

    @Test
    void code로_약속잡기_단건을_조회한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // when
        Appointment foundAppointment = appointmentRepository.findByCode(savedAppointment.getCode()).orElseThrow();

        // then
        assertThat(savedAppointment).isEqualTo(foundAppointment);
    }

    @Test
    void 약속잡기_제목이_빈값일_경우_예외를_던진다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.title("").build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 약속잡기_제목이_255자가_넘는_경우_예외를_던진다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.title("a".repeat(256)).build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 약속잡기_설명이_1000자가_넘는_경우_예외를_던진다() {
        // given
        Appointment appointment = DEFAULT_BUILDER
                .description("d".repeat(1001))
                .build();

        // when & then
        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("getAppointmentWithEachNull")
    void 약속잡기의_각각의_필드가_null이면_예외를_던진다(Function<AppointmentBuilder, AppointmentBuilder> function) {
        Appointment appointment = function.apply(DEFAULT_BUILDER).build();

        assertThatThrownBy(() -> appointmentRepository.save(appointment))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @Disabled
        // todo : fix this
    void 포뮬라를_적용해_count를_불러온다(@Autowired EntityManager entityManager) {
        // given
        Member member = memberRepository.findById(1L).orElseThrow();
        Appointment appointment = DEFAULT_BUILDER.build();
        appointmentRepository.save(appointment);

        LocalDateTime now = LocalDateTime.now();
        appointment.selectAvailableTime(Set.of(LocalDateTime.of(now.plusDays(5).toLocalDate(), LocalTime.of(15, 0)),
                LocalDateTime.of(now.plusDays(5).toLocalDate(), LocalTime.of(15, 30))), member, now);

        entityManager.flush();
        entityManager.detach(appointment);

        // when
        Appointment foundAppointment = appointmentRepository.findByCode(appointment.getCode()).orElseThrow();

        // then
        assertThat(foundAppointment.getCount()).isEqualTo(1);
    }

    @Test
    void 약속잡기를_삭제한다() {
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
                .closedAt(LocalDateTime.now().plusDays(1))
                .code(Code.generate(length -> "FJn3ND26"))
                .closedAt(LocalDateTime.now().plusDays(1))
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        appointmentRepository.delete(savedAppointment);

        // when
        Optional<Appointment> appointmentOptional = appointmentRepository.findByCode(savedAppointment.getCode());

        // then
        assertThat(appointmentOptional).isEmpty();
    }

    @Test
    void 종료할_약속잡기를_가져온다() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        List<Appointment> appointmentsToBeClosed = appointmentRepository.findAllToBeClosed(now);

        // then
        assertThat(appointmentsToBeClosed).hasSize(1);
    }

    @Test
    void 아이디_목록으로_약속잡기_목록을_종료한다(@Autowired EntityManager entityManager) {
        // given
        int iterationCount = 10;
        List<Appointment> appointments = new ArrayList<>();

        for (int i = 0; i < iterationCount; i++) {
            String code = "zxcvabc" + i;
            Appointment savedAppointment = appointmentRepository.save(
                    DEFAULT_BUILDER.code(Code.generate(ignored -> code)).build()
            );
            appointments.add(savedAppointment);
        }

        // when
        appointmentRepository.closeAllByIds(
                appointments.stream()
                        .map(Appointment::getId)
                        .collect(Collectors.toList())
        );

        for (Appointment appointment : appointments) {
            entityManager.refresh(appointment);
        }
        // then
        assertThat(appointments).allMatch(Appointment::isClosed);

    }
}
