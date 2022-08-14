package com.morak.back.appointment.application;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.AvailableTimeRepository;
import com.morak.back.appointment.domain.RankRecommendation;
import com.morak.back.appointment.domain.RecommendationCells;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.appointment.exception.AppointmentNotFoundException;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.util.HashSet;
import java.util.List;
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
    private final AvailableTimeRepository availableTimeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public String createAppointment(String teamCode, Long memberId, AppointmentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = request.toAppointment(team, member, Code.generate(CODE_GENERATOR));
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return savedAppointment.getCode();
    }

    @Transactional(readOnly = true)
    public List<AppointmentAllResponse> findAppointments(String teamCode, Long memberId) {
        Long teamId = teamRepository.findIdByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(teamId, memberId);

        return appointmentRepository.findAllByTeamId(teamId).stream()
                .map(AppointmentAllResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findAppointment(String teamCode, Long memberId, String appointmentCode) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> AppointmentNotFoundException.ofAppointment(
                        CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR, appointmentCode
                ));
        validateAppointmentInTeam(team, appointment);
        return AppointmentResponse.from(appointment);
    }

    private void validateAppointmentInTeam(Team findTeam, Appointment appointment) {
        if (!appointment.isBelongedTo(findTeam)) {
            throw new AppointmentAuthorizationException(
                    CustomErrorCode.APPOINTMENT_TEAM_MISMATCHED_ERROR,
                    String.format("%s 코드의 약속잡기는 %s 코드의 팀에 속해있지 않습니다.",
                            appointment.getCode(), findTeam.getCode()
                    )
            );
        }
    }

    public void selectAvailableTimes(String teamCode, Long memberId, String appointmentCode,
                                     List<AvailableTimeRequest> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> AppointmentNotFoundException.ofAppointment(
                        CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR, appointmentCode
                ));

        validateDuplicatedRequest(requests);
        validateAppointmentInTeam(team, appointment);
        validateAppointmentStatus(appointment);
        deleteOldAvailableTimes(memberId, appointment);

        List<AvailableTime> availableTimes = requests.stream()
                .map(request -> request.toAvailableTime(member, appointment))
                .collect(Collectors.toList());

        availableTimeRepository.saveAll(availableTimes);
    }

    private void validateAppointmentStatus(Appointment appointment) {
        if (appointment.isClosed()) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR,
                    appointment.getCode() + "코드의 약속잡기는 마감되었습니다."
            );
        }
    }

    private void validateDuplicatedRequest(List<AvailableTimeRequest> availableTimeRequest) {
        HashSet<AvailableTimeRequest> availableTimeRequestsSet = new HashSet<>(availableTimeRequest);
        if (availableTimeRequestsSet.size() != availableTimeRequest.size()) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DUPLICATED_AVAILABLE_TIME_ERROR,
                    availableTimeRequest + " 요청된 시간에 중복된 시간이 있습니다."
            );
        }
    }

    private void deleteOldAvailableTimes(Long memberId, Appointment appointment) {
        availableTimeRepository.deleteAllByMemberIdAndAppointmentId(memberId, appointment.getId());
        availableTimeRepository.flush();
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> recommendAvailableTimes(String teamCode, Long memberId,
                                                                String appointmentCode) {
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> AppointmentNotFoundException.ofAppointment(
                        CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR, appointmentCode
                ));
        validateAppointmentInTeam(team, appointment);
        List<Member> members = teamMemberRepository.findAllByTeamId(team.getId())
                .stream()
                .map(TeamMember::getMember)
                .collect(Collectors.toList());

        RecommendationCells recommendationCells = RecommendationCells.of(appointment, members);

        List<AvailableTime> availableTimes = availableTimeRepository.findAllByAppointmentId(appointment.getId());
        List<RankRecommendation> rankRecommendations = recommendationCells.recommend(availableTimes);

        return rankRecommendations.stream()
                .map(RecommendationResponse::from)
                .collect(Collectors.toList());
    }

    public void closeAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> AppointmentNotFoundException.ofAppointment(
                        CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR, appointmentCode
                ));
        validateAppointmentInTeam(team, appointment);
        appointment.close(member);
    }

    public void deleteAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> AppointmentNotFoundException.ofAppointment(
                        CustomErrorCode.APPOINTMENT_NOT_FOUND_ERROR, appointmentCode
                ));
        validateHost(member, appointment);
        validateAppointmentInTeam(team, appointment);
        appointmentRepository.deleteById(appointment.getId());
    }

    private void validateHost(Member member, Appointment appointment) {
        if (!appointment.isHost(member)) {
            throw new AppointmentAuthorizationException(
                    CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR,
                    member.getId() + "번 멤버는 " + appointment.getCode() + "코드의 약속잡기의 호스트가 아닙니다."
            );
        }
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, teamId, memberId);
        }
    }
}
