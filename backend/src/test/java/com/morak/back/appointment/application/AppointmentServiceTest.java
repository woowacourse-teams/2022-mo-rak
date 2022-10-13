package com.morak.back.appointment.application;

import static com.morak.back.appointment.domain.menu.MenuStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.menu.MenuStatus;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.appointment.exception.AppointmentNotFoundException;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AppointmentStatusResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AppointmentServiceTest {

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final FakeApiReceiver receiver;

    private final NotificationService notificationService;
    private final AppointmentService appointmentService;

    private final SystemTime systemTime;

    private AppointmentBuilder DEFAULT_BUILDER;

    private Member 에덴;
    private Team 모락;

    private Appointment 약속잡기_현재부터_1일에서_5일_14시_20시;
    private Appointment 약속잡기_현재부터_1일에서_5일_14시_0시;
    private Appointment 약속잡기_현재부터_1일에서_1일_23시_30분_부터_0시;
    private Appointment 약속잡기_현재부터_1일에서_5일_0시부터_0시;
    private Appointment 약속잡기_현재부터_1일에서_1일_0시부터_0시;
    private AvailableTime 에덴_가능_시간_선택_현재부터_1일_4시;
    private AvailableTime 에덴_가능_시간_선택_현재부터_1일_4시_반;
    private AvailableTime 에덴_가능_시간_선택_현재부터_1일_5시;

    @Autowired
    public AppointmentServiceTest(AppointmentRepository appointmentRepository,
                                  MemberRepository memberRepository, TeamRepository teamRepository,
                                  TeamMemberRepository teamMemberRepository,
                                  SlackWebhookRepository slackWebhookRepository) {
        this.appointmentRepository = appointmentRepository;
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.systemTime = new SystemTime(LocalDateTime.now());

        this.receiver = new FakeApiReceiver();
        SlackClient slackClient = new FakeSlackClient(receiver);
        this.notificationService = new NotificationService(slackClient, teamRepository, teamMemberRepository,
                        slackWebhookRepository, memberRepository);
        this.appointmentService = new AppointmentService(appointmentRepository,
                memberRepository, teamRepository, teamMemberRepository, notificationService, systemTime);
    }

    @BeforeEach
    void setUp() {
        CodeGenerator codeGenerator = new RandomCodeGenerator();

        에덴 = memberRepository.findById(1L).orElseThrow();
        모락 = teamRepository.findByCode("MoraK123").orElseThrow();
        LocalDate now = systemTime.now().toLocalDate();

        DEFAULT_BUILDER = Appointment.builder()
                .title("회식 날짜")
                .description("필참입니다.")
                .team(모락)
                .host(에덴)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .now(systemTime.now())
                .closedAt(LocalDateTime.now().plusMinutes(30));

        약속잡기_현재부터_1일에서_5일_14시_20시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();

        약속잡기_현재부터_1일에서_5일_14시_0시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(0, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();

        약속잡기_현재부터_1일에서_1일_23시_30분_부터_0시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(1))
                .startTime(LocalTime.of(23, 30))
                .endTime(LocalTime.of(0, 0))
                .durationHours(0)
                .durationMinutes(30)
                .build();

        약속잡기_현재부터_1일에서_5일_0시부터_0시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(5))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();


        약속잡기_현재부터_1일에서_1일_0시부터_0시 = DEFAULT_BUILDER
                .code(Code.generate(codeGenerator))
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(1))
                .startTime(LocalTime.of(0, 0))
                .endTime(LocalTime.of(0, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();

        에덴_가능_시간_선택_현재부터_1일_4시 = AvailableTime.builder()
                .member(에덴)
                .startDateTime(LocalDateTime.of(now.plusDays(1), LocalTime.of(16, 0)))
                .build();

        에덴_가능_시간_선택_현재부터_1일_4시_반 = AvailableTime.builder()
                .member(에덴)
                .startDateTime(LocalDateTime.of(now.plusDays(1), LocalTime.of(16, 30)))
                .build();

        에덴_가능_시간_선택_현재부터_1일_5시 = AvailableTime.builder()
                .member(에덴)
                .startDateTime(LocalDateTime.of(now.plusDays(1), LocalTime.of(17, 0)))
                .build();
    }

    @Test
    void 약속잡기를_생성한다() {
        // given
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(15),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                30,
                LocalDateTime.now().plusDays(10)
        );

        // when
        String createdAppointmentCode = appointmentService.createAppointment("MoraK123", 1L, request);

        // then
        assertThat(createdAppointmentCode).hasSize(8);
    }

    @Test
    void 약속잡기_생성_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(15),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                30,
                LocalDateTime.now().plusDays(10)
        );

        // when & then
        assertThatThrownBy(() -> appointmentService.createAppointment(모락.getCode(), 차리.getId(),
                request))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_생성_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.createAppointment(invalidTeamCode, 에덴.getId(), new AppointmentCreateRequest()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
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
        List<AppointmentAllResponse> appointmentsResponse = appointmentService.findAppointments(모락.getCode(),
                에덴.getId());

        // then
        assertThat(appointmentsResponse)
                .extracting("code")
                .containsExactly(appointment5.getCode(), appointment4.getCode(), appointment2.getCode(), "FEsd23C1",
                        appointment3.getCode(), appointment1.getCode());
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given & when
        List<AppointmentAllResponse> appointmentsResponse = appointmentService.findAppointments("MoraK123", 1L);

        // then
        assertThat(appointmentsResponse).hasSize(1);
    }

    @Test
    void 약속잡기_목록_조회_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        // when & then
        assertThatThrownBy(() -> appointmentService.findAppointments(모락.getCode(), 차리.getId()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_목록_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.findAppointments(invalidTeamCode, 에덴.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기_단건을_조회한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AppointmentResponse appointmentResponse = appointmentService.findAppointment(
                appointment.getTeamCode().getCode(), appointment.getHostId().getId(), appointment.getCode()
        );

        // then
        assertThat(appointmentResponse.getTitle()).isEqualTo("회식 날짜");
    }

    @Test
    void 약속잡기_단건_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.findAppointment(invalidTeamCode, 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표가_그룹에_속해있지_않다면_예외를_던진다() {
        // given
        Team otherTeam = teamRepository.findByCode("Betrayed").orElseThrow();
        Member otherMember = memberRepository.findById(4L).orElseThrow();
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when & then
        assertThatThrownBy(() -> appointmentService.findAppointment(
                otherTeam.getCode(), otherMember.getId(), appointment.getCode()
        )).isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_단건_조회_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        // when & then
        assertThatThrownBy(() -> appointmentService.findAppointment(모락.getCode(), 차리.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_가능시간을_선택한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatNoException().isThrownBy(
                () -> appointmentService.selectAvailableTimes(
                        appointment.getTeamCode().getCode(),
                        appointment.getHostId().getId(),
                        appointment.getCode(),
                        requests
                )
        );
    }

    @Test
    void 약속잡기_가능시간_선택_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0))
        );

        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // when & then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(모락.getCode(), 차리.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode(),
                requests))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_가능시간_선택_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.selectAvailableTimes(invalidTeamCode, 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode(),
                        List.of(new AvailableTimeRequest())))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기_가능시간을_중복으로_선택하면_하나의_선택으로_간주하고_처리한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        LocalDate now = LocalDate.now();

        // when
        AvailableTimeRequest availableTimeRequest1 = new AvailableTimeRequest(
                LocalDateTime.of(now.plusDays(1), LocalTime.of(16, 0))
        );

        AvailableTimeRequest availableTimeRequest2 = new AvailableTimeRequest(
                LocalDateTime.of(now.plusDays(1), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest1, availableTimeRequest2);

        // then
        assertThatCode(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        )).doesNotThrowAnyException();
    }

    @Test
    void 약속잡기_가능시간을_선택할_때_약속잡기가_이미_마감되었으면_예외를_던진다() {
        // given
        약속잡기_현재부터_1일에서_5일_14시_20시.close(에덴);
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(plusDay), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "13, 30, 14, 0",
            "20, 0, 20, 30"
    })
    void 중간_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int startHour, int startMinute, int endHour, int endMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(plusDay), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({"13, 30", "0, 0"})
    void 자정까지_약속잡기_가능시간을_선택할_때_약속잡기의_시간을_벗어나면_예외를_던진다(int startHour, int startMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 6L})
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_기간을_벗어나면_예외를_던진다(long plusDay) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_0시부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(plusDay), LocalTime.of(16, 0))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0, 1, 0, 30",
            "5, 23, 30, 6, 0, 0"
    })
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(
            int startPlusDay, int startHour, int startMinute, int endPlusDay, int endHour, int endMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_0시부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startPlusDay), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatNoException().isThrownBy(
                () -> appointmentService.selectAvailableTimes(
                        appointment.getTeamCode().getCode(),
                        appointment.getHostId().getId(),
                        appointment.getCode(),
                        requests
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30, 1, 0, 0",
            "6, 0, 0, 6, 0, 30"
    })
    void 하루종일_5일동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(
            int startPlusDay, int startHour, int startMinute, int endPlusDay, int endHour, int endMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_0시부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startPlusDay), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0, 1, 0, 30",
            "1, 23, 30, 2, 0, 0"
    })
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값_안쪽에_들어올_수_있다(
            int startPlusDay, int startHour, int startMinute, int endPlusDay, int endHour, int endMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_1일_0시부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startPlusDay), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatNoException().isThrownBy(
                () -> appointmentService.selectAvailableTimes(
                        appointment.getTeamCode().getCode(),
                        appointment.getHostId().getId(),
                        appointment.getCode(),
                        requests
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30",
            "2, 0, 0"
    })
    // eden: 없어져도 될 것 같은데?
    void 하루종일_하루동안_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(
            int startPlusDay, int startHour, int startMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_1일_0시부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startPlusDay), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 23, 30, 1, 0, 0", // 전혀 다른날
            "1, 0, 0, 1, 0, 30", // 같은날, 다른시간
            "1, 23, 0, 1, 23, 30", // 시작 시간 경계
            "2, 0, 0, 2, 0, 30" // 마지막 시간 경계
    })
    void 하루동안_삼십분짜리_약속잡기_가능시간을_선택할_때_약속잡기의_경계값을_벗어나면_예외를_던진다(
            int startPlusDay, int startHour, int startMinute, int endPlusDay, int endHour, int endMinute) {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_1일_23시_30분_부터_0시);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startPlusDay), LocalTime.of(startHour, startMinute))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode(),
                requests
        ))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR);
    }

    @Test
    @Disabled // todo : fix this
    void 약속잡기_가능시간_추천_결과를_조회한다() {
        // given
//        Appointment appointment = appointmentRepository.save(약속잡기_중간);
//        availableTimeRepository.saveAll(List.of(
//                회식_가능_시간_4시부터_4시반까지,
//                회식_가능_시간_4시반부터_5시까지,
//                회식_가능_시간_5시부터_5시반까지
//        ));

        // when
//        List<RecommendationResponse> recommendationResponses = appointmentService.recommendAvailableTimes(
//                appointment.getTeam().getCode(),
//                appointment.getHost().getId(),
//                appointment.getCode()
//        );
//
//        // then
//        assertThat(recommendationResponses).hasSize(6);
    }

    @Test
    void 약속잡기_가능시간_추천_결과_조회_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        // when & then
        assertThatThrownBy(
                () -> appointmentService.recommendAvailableTimes(모락.getCode(), 차리.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_가능시간_추천_결과_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.recommendAvailableTimes(invalidTeamCode, 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기를_마감한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        appointmentService.closeAppointment(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode()
        );

        // then
        assertThat(약속잡기_현재부터_1일에서_5일_14시_20시.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기_마감_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.closeAppointment(invalidTeamCode, 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기_마감_시_호스트가_아닌_경우_예외를_던진다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        Member 엘리 = memberRepository.findById(2L).orElseThrow();

        // when & then
        assertThatThrownBy(() -> appointmentService.closeAppointment(appointment.getTeamCode().getCode(), 엘리.getId(),
                appointment.getCode()))
                .isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_마감_시_약속잡기가_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidAppointmentCode = "kingEden";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.closeAppointment(모락.getCode(), 에덴.getId(), invalidAppointmentCode))
                .isInstanceOf(AppointmentNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기_마감_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        // when & then
        assertThatThrownBy(
                () -> appointmentService.closeAppointment(모락.getCode(), 차리.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기를_삭제한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        appointmentService.deleteAppointment(
                appointment.getTeamCode().getCode(),
                appointment.getHostId().getId(),
                appointment.getCode()
        );

        // then
        assertThat(appointmentRepository.findByCode(appointment.getCode())).isEmpty();
    }

    @Test
    @Disabled
    void 선택된_가능시간이_있는_약속잡기를_삭제한다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
//        availableTimeRepository.save(회식_가능_시간_4시부터_4시반까지);

        // when
        appointmentService.deleteAppointment(모락.getCode(), 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode());

        // then
//        assertThat(availableTimeRepository.findAllByAppointment(appointment)).isEmpty();
    }

    @Test
    void 약속잡기_삭제_시_호스트가_아닌_경우_예외를_던진다() {
        // given
        Appointment appointment = appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);
        Member 엘리 = memberRepository.findById(2L).orElseThrow();

        // when & then
        assertThatThrownBy(() -> appointmentService.deleteAppointment(appointment.getTeamCode().getCode(), 엘리.getId(),
                appointment.getCode()))
                .isInstanceOf(AppointmentAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_삭제_시_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.findById(4L).orElseThrow();

        // when & then
        assertThatThrownBy(
                () -> appointmentService.deleteAppointment(모락.getCode(), 차리.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 약속잡기_삭제_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "morakko";

        // when & then
        assertThatThrownBy(
                () -> appointmentService.deleteAppointment(invalidTeamCode, 에덴.getId(), 약속잡기_현재부터_1일에서_5일_14시_20시.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 약속잡기가_진행중인지_확인한다() {
        // given
        appointmentRepository.save(약속잡기_현재부터_1일에서_5일_14시_20시);

        // when
        AppointmentStatusResponse status = appointmentService.findAppointmentStatus(모락.getCode(), 에덴.getId(),
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
        AppointmentStatusResponse status = appointmentService.findAppointmentStatus(모락.getCode(), 에덴.getId(),
                약속잡기_현재부터_1일에서_5일_14시_20시.getCode());

        // then
        assertThat(status.getStatus()).isEqualTo(MenuStatus.CLOSED);
    }
}
