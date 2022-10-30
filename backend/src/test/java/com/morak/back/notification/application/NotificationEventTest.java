package com.morak.back.notification.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentEvent;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollEvent;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleHistoryEvent;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
    private static final String POLL_CODE = "testcode";
    private static final String TEAM_CODE = "MoraK123";
    private static final long MEMBER_ID = 2L;

    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final PollService pollService;
    private final PollRepository pollRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final SystemTime systemTime;
    private final AnyEventListener eventListener;

    @Autowired
    public NotificationEventTest(
            AppointmentService appointmentService,
            AppointmentRepository appointmentRepository,
            PollService pollService,
            PollRepository pollRepository,
            RoleService roleService,
            RoleRepository roleRepository,
            SystemTime systemTime,
            AnyEventListener eventListener
    ) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.pollService = pollService;
        this.pollRepository = pollRepository;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.systemTime = systemTime;
        this.eventListener = eventListener;
    }

    @BeforeEach
    void setUp() {
        eventListener.clear();
    }

    @Test
    void 투표를_생성하면_알림을_보낸다() {
        // given
        PollCreateRequest request = new PollCreateRequest(
                "투표_제목",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2")
        );
        // when
        PollResponse response = pollService.createPoll(TEAM_CODE, MEMBER_ID, request);
        // then
        Assertions.assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(eventListener.hasEvent(PollEvent.class)).isTrue()
        );
    }

    @Test
    void 투표를_마감하면_알림을_보낸다() {
        // given & when
        pollService.closePoll(TEAM_CODE, MEMBER_ID, POLL_CODE);

        // then
        Poll poll = pollRepository.findByCode(POLL_CODE).orElseThrow();
        Assertions.assertAll(
                () -> assertThat(poll.isClosed()).isTrue(),
                () -> assertThat(eventListener.hasEvent(PollEvent.class)).isTrue()
        );
    }

    @Test
    void 마감시각에_해당하는_투표를_종료하면_알림을_보낸다() {
        // given & when
        pollService.closeAllBeforeNow();

        // then
        assertThat(eventListener.hasEvent(PollEvent.class)).isTrue();
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
        AppointmentResponse response = appointmentService.createAppointment(TEAM_CODE, 1L, request);
        // then
        Assertions.assertAll(
                () -> assertThat(response.getId()).isNotNull(),
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

    @Test
    void 역할정하기_매칭을하면_알림을_보낸다() {
        // given
        Role role = new Role(TEAM_CODE, RoleNames.from(List.of("데일리 마스터", "서기")), new RoleHistories());
        roleRepository.save(role);
        // when
        roleService.matchRoleAndMember(TEAM_CODE, MEMBER_ID);
        // then
        assertThat(eventListener.hasEvent(RoleHistoryEvent.class)).isTrue();
    }
}
