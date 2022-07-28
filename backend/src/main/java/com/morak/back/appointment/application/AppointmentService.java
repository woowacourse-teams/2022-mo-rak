package com.morak.back.appointment.application;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.AvailableTimeRepository;
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
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.MismatchedTeamException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailableTimeRepository availableTimeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public String createAppointment(String teamCode, Long memberId, AppointmentCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(team.getId(), memberId);

        Appointment appointment = request.toAppointment(team, member);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return savedAppointment.getCode();
    }

    public List<AppointmentAllResponse> findAppointments(String teamCode, Long memberId) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        return appointmentRepository.findAllByTeamId(teamId).stream()
                .distinct()
                .map(AppointmentAllResponse::from)
                .collect(Collectors.toList());
    }

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

        availableTimeRepository.deleteAllByMemberIdAndAppointmentId(memberId, appointment.getId());

        List<AvailableTime> availableTimes = requests.stream()
                .map(request -> request.toAvailableTime(member, appointment))
                .collect(Collectors.toList());

        saveAllAvailableTimes(availableTimes);
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

    private void saveAllAvailableTimes(List<AvailableTime> availableTimes) {
        // TODO: 2022/07/28 equals, hashcode를 오버라이드(아마 hashcode는 커스텀으로 해야할지도!) 하는 방법으로 변경해야함!!
        try {
            availableTimeRepository.saveAll(availableTimes);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("동일한 약속잡기 가능 시간을 선택할 수 없습니다.");
        }
    }

    public List<RecommendationResponse> recommendAvailableTimes(String teamCode, Long memberId,
                                                                String appointmentCode) {
        Long teamId = teamRepository.findIdByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateMemberInTeam(teamId, memberId);

        Appointment appointment = appointmentRepository.findByCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        return null;
    }
}
