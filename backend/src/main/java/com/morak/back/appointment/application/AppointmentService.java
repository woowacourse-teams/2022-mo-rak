package com.morak.back.appointment.application;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.AvailableTimeRepository;
import com.morak.back.appointment.domain.RankRecommendation;
import com.morak.back.appointment.domain.RecommendationCells;
import com.morak.back.appointment.exception.AppointmentNotFoundException;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.auth.exception.TeamNotFoundException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.MismatchedTeamException;
import java.time.Duration;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = request.toAppointment(team, member, Code.generate(CODE_GENERATOR));
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return savedAppointment.getCode();
    }

    @Transactional(readOnly = true)
    public List<AppointmentAllResponse> findAppointments(String teamCode, Long memberId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        return appointmentRepository.findAllByTeamId(teamId).stream()
                .distinct()
                .map(AppointmentAllResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findAppointment(String teamCode, Long memberId, String appointmentCode) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        return AppointmentResponse.from(appointment);
    }

    public void selectAvailableTimes(String teamCode, Long memberId, String appointmentCode,
                                     List<AvailableTimeRequest> requests) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));

        validateAppointmentStatus(appointment);

        deleteOldAvailableTimes(memberId, appointment);

        validateDuplicatedRequest(requests);

        List<AvailableTime> availableTimes = requests.stream()
                .map(request -> request.toAvailableTime(member, appointment))
                .collect(Collectors.toList());

        availableTimeRepository.saveAll(availableTimes);
    }

    private void validateDuplicatedRequest(List<AvailableTimeRequest> availableTimeRequest) {
        HashSet<AvailableTimeRequest> availableTimeRequestsSet = new HashSet<>(availableTimeRequest);
        if (availableTimeRequestsSet.size() != availableTimeRequest.size()) {
            throw new InvalidRequestException("중복된 약속잡기 가능 시간은 허용되지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> recommendAvailableTimes(String teamCode, Long memberId,
                                                                String appointmentCode) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        List<Member> members = teamMemberRepository.findAllByTeamId(teamId)
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
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        appointment.close(member);
    }

    public void deleteAppointment(String teamCode, Long memberId, String appointmentCode) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        appointment.validateHost(member);

        appointmentRepository.deleteById(appointment.getId());
    }

    private void validateMemberInTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new MismatchedTeamException(teamId, memberId);
        }
    }

    private void validateAppointmentStatus(Appointment appointment) {
        if (appointment.isClosed()) {
            throw new InvalidRequestException(appointment.getId() + "번 약속잡기는 마감되었습니다.");
        }
    }

    private void deleteOldAvailableTimes(Long memberId, Appointment appointment) {
        availableTimeRepository.deleteAllByMemberIdAndAppointmentId(memberId, appointment.getId());
        availableTimeRepository.flush();
    }
}
