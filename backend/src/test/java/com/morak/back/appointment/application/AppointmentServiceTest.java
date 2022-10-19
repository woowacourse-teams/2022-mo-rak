package com.morak.back.appointment.application;

import static com.morak.back.appointment.domain.menu.MenuStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.appointment.domain.menu.MenuStatus;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AppointmentStatusResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AppointmentServiceTest {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentService appointmentService;

    private final SystemTime systemTime;

    private AppointmentBuilder DEFAULT_BUILDER;

    private Long 에덴;
    private String 모락;
    private LocalDateTime now;
    private LocalDate today;

    private Appointment 약속잡기_현재부터_1일에서_5일_14시_20시;

    @Autowired
    public AppointmentServiceTest(AppointmentRepository appointmentRepository,
                                  MemberRepository memberRepository, TeamRepository teamRepository,
                                  TeamMemberRepository teamMemberRepository,
                                  SlackWebhookRepository slackWebhookRepository) {
        this.appointmentRepository = appointmentRepository;
        this.systemTime = new SystemTime(LocalDateTime.now());

        SlackClient slackClient = new FakeSlackClient(new FakeApiReceiver());
        NotificationService notificationService = new NotificationService(slackClient, teamRepository,
                teamMemberRepository,
                slackWebhookRepository, memberRepository);
        this.appointmentService = new AppointmentService(appointmentRepository,
                memberRepository, teamRepository, teamMemberRepository, notificationService, systemTime);
    }

    @BeforeEach
    void setUp() {
        CodeGenerator codeGenerator = new RandomCodeGenerator();

        모락 = "MoraK123";
        에덴 = 1L;
        now = systemTime.now();
        today = now.toLocalDate();

        DEFAULT_BUILDER = Appointment.builder()
                .title("회식 날짜")
                .description("필참입니다.")
                .teamCode(모락)
                .hostId(에덴)
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .now(systemTime.now())
                .closedAt(now.plusMinutes(30));

        약속잡기_현재부터_1일에서_5일_14시_20시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();
    }

    @Test
    void 약속잡기를_생성한다() {
        // given
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                now.toLocalDate().plusDays(5),
                now.toLocalDate().plusDays(15),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                30,
                now.plusDays(10)
        );

        // when
        String createdAppointmentCode = appointmentService.createAppointment("MoraK123", 1L, request);

        // then
        assertThat(createdAppointmentCode).hasSize(8);
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given & when
        List<AppointmentAllResponse> appointmentsResponse = appointmentService.findAppointments("MoraK123", 1L);

        // then
        assertThat(appointmentsResponse).hasSize(1);
    }

    @Test
    void 약속잡기_목록_조회_시_진행중인_약속잡기가_종료된_약속잡기보다_먼저_조회된다() {
        // given
        Appointment appointment1 = appointmentRepository.save(
                DEFAULT_BUILDER.code(Code.generate((l) -> "appment1")).build());
        Appointment appointment2 = appointmentRepository.save(
                DEFAULT_BUILDER.code(Code.generate((l) -> "appment2")).build());
        Appointment appointment3 = appointmentRepository.save(
                DEFAULT_BUILDER.code(Code.generate((l) -> "appment3")).build());
        Appointment appointment4 = appointmentRepository.save(
                DEFAULT_BUILDER.code(Code.generate((l) -> "appment4")).build());
        Appointment appointment5 = appointmentRepository.save(
                DEFAULT_BUILDER.code(Code.generate((l) -> "appment5")).build());

        appointment1.close(에덴);
        appointment3.close(에덴);

        // when
        List<AppointmentAllResponse> appointmentsResponse = appointmentService.findAppointments(모락, 에덴);

        // then
        assertThat(appointmentsResponse)
                .extracting("code")
                .containsExactly(appointment5.getCode(), appointment4.getCode(), appointment2.getCode(), "FEsd23C1",
                        appointment3.getCode(), appointment1.getCode());
    }

    @Test
    void 약속잡기_단건을_조회한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AppointmentResponse actual = appointmentService.findAppointment(
                appointment.getTeamCode(), appointment.getHostId(), appointment.getCode()
        );

        AppointmentResponse expected = AppointmentResponse.from(appointment, 1L);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 약속잡기_가능시간을_선택한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.of(16, 0))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatNoException().isThrownBy(
                () -> appointmentService.selectAvailableTimes(
                        appointment.getTeamCode(),
                        appointment.getHostId(),
                        appointment.getCode(),
                        requests
                )
        );
    }

    @Test
    void 약속잡기_가능시간을_중복으로_선택하면_하나의_선택으로_간주한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AvailableTimeRequest availableTimeRequest1 = new AvailableTimeRequest(
                LocalDateTime.of(today.plusDays(1), LocalTime.of(16, 0))
        );

        AvailableTimeRequest availableTimeRequest2 = new AvailableTimeRequest(
                LocalDateTime.of(today.plusDays(1), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest1, availableTimeRequest2);

        // then
        assertThatNoException().isThrownBy(
                () -> appointmentService.selectAvailableTimes(
                        appointment.getTeamCode(),
                        appointment.getHostId(),
                        appointment.getCode(),
                        requests
                )
        );
    }

    @Test
    void 약속잡기_가능시간_추천_결과를_조회한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        LocalDateTime select1 = LocalDateTime.of(today.plusDays(1), LocalTime.of(14, 0));
        LocalDateTime select2 = LocalDateTime.of(today.plusDays(1), LocalTime.of(14, 30));
        LocalDateTime select3 = LocalDateTime.of(today.plusDays(1), LocalTime.of(15, 0));
        LocalDateTime select4 = LocalDateTime.of(today.plusDays(1), LocalTime.of(15, 30));

        appointment.selectAvailableTime(Set.of(select1, select2, select3, select4), 에덴, now);

        // when
        List<RecommendationResponse> recommendationResponses = appointmentService.recommendAvailableTimes(
                appointment.getTeamCode(),
                appointment.getHostId(),
                appointment.getCode()
        );

        // then
        assertAll(
                () -> assertThat(recommendationResponses).hasSize(4),
                () -> assertThat(recommendationResponses)
                        .extracting("rank", "recommendStartDateTime", "recommendEndDateTime")
                        .containsExactly(
                                tuple(1, select1, select4.plusMinutes(30)),
                                tuple(2, select2, select4.plusMinutes(60)),
                                tuple(3, select3, select4.plusMinutes(90)),
                                tuple(4, select4, select4.plusMinutes(120))
                        )
        );
    }

    @Test
    void 약속잡기를_마감한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        appointmentService.closeAppointment(
                appointment.getTeamCode(),
                appointment.getHostId(),
                appointment.getCode()
        );

        // then
        assertThat(약속잡기_현재부터_1일에서_5일_14시_20시.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기를_삭제한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        appointmentService.deleteAppointment(
                appointment.getTeamCode(),
                appointment.getHostId(),
                appointment.getCode()
        );

        // then
        assertThat(appointmentRepository.findByCode(appointment.getCode())).isEmpty();
    }

    @Test
    void 약속잡기가_진행중인지_확인한다() {
        // given
        appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AppointmentStatusResponse status = appointmentService.findAppointmentStatus(모락, 에덴,
                약속잡기_현재부터_1일에서_5일_14시_20시.getCode());

        // then
        assertThat(status.getStatus()).isEqualTo(MenuStatus.OPEN);
    }

    @Test
    void 약속잡기가_마감되었는지_확인한다() {
        // given
        약속잡기_현재부터_1일에서_5일_14시_20시.close(약속잡기_현재부터_1일에서_5일_14시_20시.getHostId());
        appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AppointmentStatusResponse status = appointmentService.findAppointmentStatus(모락, 에덴,
                약속잡기_현재부터_1일에서_5일_14시_20시.getCode());

        // then
        assertThat(status.getStatus()).isEqualTo(MenuStatus.CLOSED);
    }
}
