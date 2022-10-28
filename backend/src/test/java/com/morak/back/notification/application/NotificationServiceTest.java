package com.morak.back.notification.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.AppointmentEvent;
import com.morak.back.appointment.domain.SystemTime;
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
    private SystemTime systemTime;

    @Autowired
    public NotificationServiceTest(
            NotificationService notificationService,
            FakeApiReceiver fakeApiReceiver,
            SystemTime systemTime
    ) {
        this.notificationService = notificationService;
        this.fakeApiReceiver = fakeApiReceiver;
        this.systemTime = systemTime;
    }

    @Test
    void 약속잡기가_생성되었다는_알림을_보낸다() {
        // given
        AppointmentEvent event = new AppointmentEvent(
                APPOINTMENT_CODE,
                TEAM_CODE,
                "제목",
                systemTime.now().plusDays(1),
                false
        );
        // when
        notificationService.notifyTeamAppointmentOpen(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }

    @Test
    void 약속잡기가_종료되었다는_알림을_보낸다() {
        // given
        AppointmentEvent event = new AppointmentEvent(
                APPOINTMENT_CODE,
                TEAM_CODE,
                "제목",
                systemTime.now().plusDays(1),
                false
        );
        // when
        notificationService.notifyTeamAppointmentClosed(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }
}