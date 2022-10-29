package com.morak.back.notification.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentEvent;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
public class NotificationEventTest {

    private static final String APPOINTMENT_CODE = "FEsd23C1";
    private static final String TEAM_CODE = "MoraK123";
    private static final long MEMBER_ID = 2L;

    private AppointmentService appointmentService;
    private AppointmentRepository appointmentRepository;
    private SystemTime systemTime;
    private AnyEventListener eventListener;

    @Autowired
    public NotificationEventTest(
            AppointmentService appointmentService,
            AppointmentRepository appointmentRepository,
            SystemTime systemTime,
            AnyEventListener eventListener
    ) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.systemTime = systemTime;
        this.eventListener = eventListener;
    }

    @BeforeEach
    void setUp() {
        eventListener.clear();
    }

    @Test
    void 약속잡기를_생성하면_알림을_보낸다() {
        // given
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "제목",
                "부제목",
                systemTime.now().toLocalDate(),
                systemTime.now().toLocalDate().plusDays(3),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                0,
                systemTime.now().plusDays(2)
        );
        // when
        AppointmentResponse appointment = appointmentService.createAppointment(TEAM_CODE, 1L, request);
        // then
        Assertions.assertAll(
                () -> assertThat(appointment.getId()).isNotNull(),
                () -> assertThat(eventListener.hasEvent(AppointmentEvent.class)).isTrue()
        );
    }

    @Test
    void 약속잡기를_종료하면_알림을_보낸다() {
        // given & when
        appointmentService.closeAppointment(TEAM_CODE, MEMBER_ID, APPOINTMENT_CODE);
        // then
        Appointment appointment = appointmentRepository.findByCode(APPOINTMENT_CODE).orElseThrow();
        Assertions.assertAll(
                () -> assertThat(appointment.isClosed()).isTrue(),
                () -> assertThat(eventListener.hasEvent(AppointmentEvent.class)).isTrue()
        );
    }

    @Test
    void 마감시각에_해당하는_약속잡기를_종료하면_알림을_보낸다() {
        // given & when
        appointmentService.closeAllBeforeNow();

        // then
        assertThat(eventListener.hasEvent(AppointmentEvent.class)).isTrue();
    }
}
