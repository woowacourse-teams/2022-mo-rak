package com.morak.back.appointment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.support.ServiceTest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ServiceTest
@ActiveProfiles("master")
class AppointmentSchedulerTest {

    private final AppointmentScheduler appointmentScheduler;
    private final AppointmentRepository appointmentRepository;
    private final SystemTime systemTime;

    @Autowired
    public AppointmentSchedulerTest(
            AppointmentScheduler appointmentScheduler,
            AppointmentRepository appointmentRepository,
            SystemTime systemTime
    ) {
        this.appointmentScheduler = appointmentScheduler;
        this.appointmentRepository = appointmentRepository;
        this.systemTime = systemTime;
    }

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = systemTime.now();
    }

    @Test
    void 스케줄링에_의해_약속잡기를_마감한다(@Autowired EntityManager entityManager) {
        // given
        LocalDateTime past = now.minusMinutes(1);

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .title("스터디 회의 날짜 정하기")
                .subTitle("필참!!")
                .startDate(now.toLocalDate().plusDays(1))
                .endDate(now.toLocalDate().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 30))
                .durationHours(1)
                .durationMinutes(0)
                .code(Code.generate(length -> "MyCode12"))
                .closedAt(past)
                .hostId(1L)
                .teamCode(Code.generate((length) -> "MoraK123"))
                .now(past.minusDays(1L))
                .build()
        );

        // when
        appointmentScheduler.scheduleAppointment();
        entityManager.flush();
        entityManager.refresh(appointment);

        // then
        assertThat(appointment.getStatus()).isEqualTo(MenuStatus.CLOSED.name());
    }
}
