package com.morak.back.notification.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.AppointmentEvent;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.notification.domain.slack.FakeApiReceiver;
import com.morak.back.poll.domain.PollEvent;
import com.morak.back.role.domain.RoleHistoryEvent;
import com.morak.back.support.ServiceTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class NotificationServiceTest {

    private static final String APPOINTMENT_CODE = "FEsd23C1";
    private static final String POLL_CODE = "testcode";
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
    void 투표가_생성되었다는_알림을_보낸다() {
        // given
        PollEvent pollEvent = new PollEvent(POLL_CODE, TEAM_CODE, "투표제목", systemTime.now().plusDays(1), false);
        // when
        notificationService.notifyTeamPollOpen(pollEvent);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }

    @Test
    void 투표가_마감되었다는_알림을_보낸다() {
        // given
        PollEvent pollEvent = new PollEvent(POLL_CODE, TEAM_CODE, "투표제목", systemTime.now().minusDays(1), true);
        // when
        notificationService.notifyTeamPollClosed(pollEvent);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
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
                systemTime.now().minusDays(1),
                true
        );
        // when
        notificationService.notifyTeamAppointmentClosed(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }

    @Test
    void 역할정하기_매칭결과_알림을_보낸다() {
        // given
        RoleHistoryEvent event = new RoleHistoryEvent(
                TEAM_CODE,
                systemTime.now(),
                Map.of(1L, "데일리마스터", 2L, "서기")
        );
        // when
        notificationService.notifyTeamRoleHistory(event);
        // then
        assertThat(fakeApiReceiver.getMessage()).isNotEmpty();
    }
}
