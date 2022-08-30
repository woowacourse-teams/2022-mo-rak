package com.morak.back.appointment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AppointmentStatus;
import com.morak.back.appointment.domain.AvailableTimeRepository;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AppointmentSchedulerTest {

    private final AppointmentRepository appointmentRepository;
    private final FakeApiReceiver receiver;
    private final AppointmentScheduler appointmentScheduler;

    @Autowired
    public AppointmentSchedulerTest(AppointmentRepository appointmentRepository,
                                    AvailableTimeRepository availableTimeRepository,
                                    MemberRepository memberRepository, TeamRepository teamRepository,
                                    TeamMemberRepository teamMemberRepository,
                                    SlackWebhookRepository slackWebhookRepository) {
        this.appointmentRepository = appointmentRepository;
        this.receiver = new FakeApiReceiver();
        SlackClient slackClient = new FakeSlackClient(receiver);

        NotificationService notificationService = new NotificationService(
                slackClient,
                teamRepository,
                teamMemberRepository,
                slackWebhookRepository
        );
        this.appointmentScheduler = new AppointmentScheduler(
                new AppointmentService(
                        appointmentRepository,
                        availableTimeRepository,
                        memberRepository,
                        teamRepository,
                        teamMemberRepository,
                        notificationService)
        );
    }

    @Test
    void 스케줄링에_의해_약속잡기를_종료한다(@Autowired EntityManager entityManager) {
        // given
        Appointment appointment = appointmentRepository.findByCode("FEsd23C1").orElseThrow();

        // when
        appointmentScheduler.scheduleAppointment();
        entityManager.refresh(appointment);

        // then
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CLOSED);
        String message = receiver.getMessage();
        assertThat(message).contains("마감");
        System.out.println("message = " + message);
    }
}
