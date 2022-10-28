package com.morak.back.notification.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.AppointmentClosedEvent;
import com.morak.back.appointment.domain.AppointmentOpenEvent;
import com.morak.back.notification.domain.slack.FakeApiReceiver;
import com.morak.back.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class NotificationServiceTest {

    private static final String APPOINTMENT_CODE = "FEsd23C1";
    private static final String TEAM_CODE = "MoraK123";

    private NotificationService notificationService;
    private FakeApiReceiver fakeApiReceiver;

    @Autowired
    public NotificationServiceTest(NotificationService notificationService, FakeApiReceiver fakeApiReceiver) {
        this.notificationService = notificationService;
        this.fakeApiReceiver = fakeApiReceiver;
    }

    @Test
    void 약속잡기가_생성되었다는_알림을_보낸다() {
        // given
        AppointmentOpenEvent event = new AppointmentOpenEvent(APPOINTMENT_CODE, TEAM_CODE);
        // when
        notificationService.notifyTeamAppointmentOpen(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }

    @Test
    void 약속잡기가_종료되었다는_알림을_보낸다() {
        // given
        AppointmentClosedEvent event = new AppointmentClosedEvent(APPOINTMENT_CODE, TEAM_CODE);
        // when
        notificationService.notifyTeamAppointmentClosed(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }
}
