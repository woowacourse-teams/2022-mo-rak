package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class AppointmentRepositoryTest {

    private static AppointmentBuilder DEFAULT_BUILDER;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Long memberId;
    private String teamCode;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        teamCode = "MoraK123";

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        DEFAULT_BUILDER = Appointment.builder()
                .title("스터디 회의 날짜 정하기")
                .subTitle("필참!!")
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .closedAt(now.plusDays(1))
                .code(Code.generate(length -> "FJn3ND26"))
                .closedAt(now.plusDays(1))
                .hostId(memberId)
                .teamCode(Code.generate((length) -> teamCode))
                .now(now);
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
        List<Appointment> appointments = appointmentRepository.findAllByTeamCode(teamCode);

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
    void 약속잡기를_삭제한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

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
}
