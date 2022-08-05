package com.morak.back.appointment.application;

import static com.morak.back.appointment.domain.AppointmentStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.morak.back.appointment.FakeCodeGenerator;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.AvailableTimeRepository;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AvailableTimeRepository availableTimeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private final Member 에덴 = Member.builder().id(1L).build();
    private final Team 모락 = Team.builder().
            id(1L)
            .code(Code.generate(new FakeCodeGenerator()))
            .build();
    private final Appointment 회식_날짜_약속잡기 = Appointment.builder()
            .id(101L)
            .host(에덴)
            .team(모락)
            .title("회식 날짜")
            .description("필참입니다.")
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(5))
            .startTime(LocalTime.of(14, 0))
            .endTime(LocalTime.of(20, 0))
            .durationHours(2)
            .durationMinutes(0)
            .code(Code.generate(new FakeCodeGenerator()))
            .build();

    private final Appointment 스터디_날짜_약속잡기 = Appointment.builder()
            .host(에덴)
            .team(모락)
            .title("스터디 날짜")
            .description("하지말까요?")
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(5))
            .startTime(LocalTime.of(14, 0))
            .endTime(LocalTime.of(20, 0))
            .durationHours(2)
            .durationMinutes(0)
            .code(Code.generate(new FakeCodeGenerator()))
            .build();

    private final AvailableTime 회식_가능_시간_4시부터_4시반까지 = AvailableTime.builder()
            .member(에덴)
            .appointment(회식_날짜_약속잡기)
            .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
            .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30)))
            .build();

    private final AvailableTime 회식_가능_시간_4시반부터_5시까지 = AvailableTime.builder()
            .member(에덴)
            .appointment(회식_날짜_약속잡기)
            .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
            .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30)))
            .build();

    private final AvailableTime 회식_가능_시간_5시부터_5시반까지 = AvailableTime.builder()
            .member(에덴)
            .appointment(회식_날짜_약속잡기)
            .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
            .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30)))
            .build();

    @Test
    void 약속잡기를_생성한다() {
        // given
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                LocalDate.of(2022, 8, 5),
                LocalDate.of(2022, 8, 20),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                30
        );

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(모락));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.save(any(Appointment.class))).willReturn(회식_날짜_약속잡기);

        // when
        String createdAppointmentCode = appointmentService.createAppointment("MoraK123", 1L, request);

        // then
        assertThat(createdAppointmentCode).hasSize(8);
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findAllByTeamId(anyLong())).willReturn(List.of(회식_날짜_약속잡기, 스터디_날짜_약속잡기));

        // when
        List<AppointmentAllResponse> appointmentsResponse = appointmentService.findAppointments("MoraK123", 1L);

        // then
        assertThat(appointmentsResponse).hasSize(2);
    }

    @Test
    void 약속잡기_단건을_조회한다() {
        // given
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));

        // when
        AppointmentResponse appointmentResponse = appointmentService.findAppointment(모락.getCode(), 1L,
                회식_날짜_약속잡기.getCode());

        // then
        assertThat(appointmentResponse.getTitle()).isEqualTo("회식 날짜");
    }

    @Test
    void 약속잡기_가능시간을_선택한다() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));

        // when
        String teamCode = "teamCode";
        Long memberId = 에덴.getId();
        String appointmentCode = "appointmentCode";
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);
        appointmentService.selectAvailableTimes(teamCode, memberId, appointmentCode, requests);

        // then
        verify(availableTimeRepository).saveAll(anyList());
    }

    @Test
    void 약속잡기_가능시간을_중복으로_선택하면_예외를_던진다() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));

        // when
        String teamCode = "teamCode";
        Long memberId = 에덴.getId();
        String appointmentCode = "appointmentCode";

        AvailableTimeRequest availableTimeRequest1 = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30))
        );

        AvailableTimeRequest availableTimeRequest2 = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest1, availableTimeRequest2);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(teamCode, memberId, appointmentCode, requests))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("중복된 약속잡기 가능 시간은 허용되지 않습니다.");
    }

    @Test
    void 약속잡기_가능시간을_선택할_때_약속잡기가_이미_마감되었으면_예외를_던진다() {
        // given
        회식_날짜_약속잡기.close(에덴);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));
        // when
        String teamCode = "teamCode";
        Long memberId = 에덴.getId();
        String appointmentCode = "appointmentCode";
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30))
        );
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);

        // then
        assertThatThrownBy(() -> appointmentService.selectAvailableTimes(teamCode, memberId, appointmentCode, requests))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("번 약속잡기는 마감되었습니다.");
    }

    @Test
    void 약속잡기_가능시간_추천_결과를_조회한다() {
        // given
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));
        given(teamMemberRepository.findAllByTeamId(anyLong())).willReturn(List.of(new TeamMember(1L, 모락, 에덴)));

        List<AvailableTime> availableTimes = List.of(회식_가능_시간_4시반부터_5시까지, 회식_가능_시간_4시반부터_5시까지,
                회식_가능_시간_5시부터_5시반까지);
        given(availableTimeRepository.findAllByAppointmentId(anyLong())).willReturn(availableTimes);

        // when
        List<RecommendationResponse> recommendationResponses = appointmentService.recommendAvailableTimes(모락.getCode(),
                에덴.getId(), 회식_날짜_약속잡기.getCode());

        // then
        assertThat(recommendationResponses).hasSize(4);
    }

    @Test
    void 약속잡기를_마감한다() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));

        //when
        appointmentService.closeAppointment(모락.getCode(), 1L, 회식_날짜_약속잡기.getCode());

        //then
        assertThat(회식_날짜_약속잡기.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 약속잡기를_삭제한다() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(에덴));
        given(teamRepository.findIdByCode(anyString())).willReturn(Optional.of(모락.getId()));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(appointmentRepository.findByCode(anyString())).willReturn(Optional.of(회식_날짜_약속잡기));

        //when
        appointmentService.deleteAppointment(모락.getCode(), 에덴.getId(), 회식_날짜_약속잡기.getCode());

        //then
        verify(appointmentRepository).deleteById(anyLong());
    }
}
