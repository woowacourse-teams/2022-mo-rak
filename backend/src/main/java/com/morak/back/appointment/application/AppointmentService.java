package com.morak.back.appointment.application;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.appointment.domain.recommend.RankRecommendation;
import com.morak.back.appointment.domain.recommend.RecommendationCells;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentNotFoundException;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AppointmentStatusResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.slack.FormattableData;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private static final CodeGenerator CODE_GENERATOR = new RandomCodeGenerator();

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final NotificationService notificationService;

    private final SystemTime systemTime;

    public String createAppointment(String teamCode, Long memberId, AppointmentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        Appointment appointment = appointmentRepository.save(
                request.toAppointment(teamCode, memberId, Code.generate(CODE_GENERATOR), systemTime.now()));
        notificationService.notifyMenuStatus(team, MessageFormatter.formatOpen(FormattableData.from(appointment)));

        return appointment.getCode();
    }

    @Transactional(readOnly = true)
    public List<AppointmentAllResponse> findAppointments(String teamCode, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        return appointmentRepository.findAllByTeamCode(teamCode)
                .stream()
                .map(AppointmentAllResponse::from)
                .sorted()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        return withTeamValidation(
                appointment -> AppointmentResponse.from(appointment, memberId),
                teamCode,
                appointmentCode
        );
    }


    private void validateAppointmentInTeam(String teamCode, Appointment appointment) {
        if (!appointment.isBelongedTo(teamCode)) {
            throw new AppointmentAuthorizationException(CustomErrorCode.APPOINTMENT_TEAM_MISMATCHED_ERROR,
                    String.format("%s 코드의 약속잡기는 %s 코드의 팀에 속해있지 않습니다.", appointment.getCode(), teamCode));
        }
    }

    public void selectAvailableTimes(String teamCode, Long memberId, String appointmentCode,
                                     List<AvailableTimeRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        withTeamValidation(
                appointment -> {
                    appointment.selectAvailableTime(
                            requests.stream().map(AvailableTimeRequest::getStart).collect(Collectors.toSet()),
                            memberId,
                            systemTime.now());
                    return null;
                }, teamCode, appointmentCode);
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> recommendAvailableTimes(String teamCode, Long memberId,
                                                                String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        return withTeamValidation(appointment -> {
            List<Member> members = teamMemberRepository.findAllByTeam(team)
                    .stream()
                    .map(TeamMember::getMember)
                    .collect(Collectors.toList());

            RecommendationCells cells = RecommendationCells.of(appointment, members.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList()));
            List<RankRecommendation> recommendations = cells.recommend(appointment.getAvailableTimes());

            return recommendations.stream()
                    .map(recommendation -> RecommendationResponse.of(recommendation, members))
                    .collect(Collectors.toList());
        }, teamCode, appointmentCode);
    }

    public void closeAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        withTeamValidation(appointment -> {
            appointment.close(memberId);
            notificationService.notifyMenuStatus(team,
                    MessageFormatter.formatClosed(FormattableData.from(appointment)));
            return null;
        }, teamCode, appointmentCode);
    }

    public void deleteAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        withTeamValidation(appointment -> {
                    validateHost(memberId, appointment);
                    appointmentRepository.delete(appointment);
                    return null;
                }, teamCode, appointmentCode
        );
    }

    private void validateHost(Long memberId, Appointment appointment) {
        if (!appointment.isHost(memberId)) {
            throw new AppointmentAuthorizationException(CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + appointment.getCode() + "코드의 약속잡기의 호스트가 아닙니다.");
        }
    }

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(),
                    member.getId());
        }
    }

    @Generated
    public void notifyClosedBySchedule() {
        List<Appointment> appointmentsToBeClosed = appointmentRepository.findAllToBeClosed(LocalDateTime.now());

        closeAll(appointmentsToBeClosed);
        notifyStatusAll(appointmentsToBeClosed);
    }

    private void closeAll(List<Appointment> appointmentsToBeClosed) {
        appointmentRepository.closeAllByIds(
                appointmentsToBeClosed.stream().map(Appointment::getId).collect(Collectors.toList()));
    }

    private void notifyStatusAll(List<Appointment> appointmentsToBeClosed) {
        Map<String, String> teamMessages = appointmentsToBeClosed.stream().collect(
                Collectors.groupingBy(
                        appointment -> appointment.getTeamCode().getCode(),
                        Collectors.mapping(
                                appointment -> MessageFormatter.formatClosed(FormattableData.from(appointment)),
                                Collectors.joining("\n")
                        )
                )
        );
        notificationService.notifyAllMenuStatus(teamMessages);
    }

    public AppointmentStatusResponse findAppointmentStatus(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        return withTeamValidation(
                appointment -> new AppointmentStatusResponse(appointment.getStatus()),
                teamCode,
                appointmentCode
        );
    }

    public <R> R withTeamValidation(Function<Appointment, R> function, String teamCode, String appointmentCode) {
        Appointment appointment = appointmentRepository.findByCode(appointmentCode).orElseThrow(
                () -> AppointmentNotFoundException.ofAppointment(CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR,
                        appointmentCode));
        validateAppointmentInTeam(teamCode, appointment);

        return function.apply(appointment);
    }
}
