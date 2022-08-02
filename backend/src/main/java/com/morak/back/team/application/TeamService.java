package com.morak.back.team.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.auth.exception.TeamNotFoundException;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import com.morak.back.team.exception.TeamInvitationNotFoundException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private static final CodeGenerator CODE_GENERATOR = new RandomCodeGenerator();

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    public String createTeam(Long memberId, TeamCreateRequest request) {
        Team team = Team.builder()
                .name(request.getName())
                .code(Code.generate(CODE_GENERATOR))
                .build();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Team savedTeam = teamRepository.save(team);

        TeamMember teamMember = TeamMember.builder()
                .team(savedTeam)
                .member(member)
                .build();
        teamMemberRepository.save(teamMember);

        return savedTeam.getCode();
    }

    public String createInvitationCode(Long memberId, String teamCode) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateJoined(team.getId(), memberId);

        TeamInvitation savedTeamInvitation = teamInvitationRepository.save(
                TeamInvitation.builder()
                        .code(Code.generate(CODE_GENERATOR))
                        .team(team)
                        .build()
        );
        return savedTeamInvitation.getCode();
    }

    private void validateJoined(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new MismatchedTeamException(teamId, memberId);
        }
    }

    @Transactional(readOnly = true)
    public InvitationJoinedResponse isJoined(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository
                .findByCode(invitationCode)
                .orElseThrow(() -> new TeamInvitationNotFoundException(invitationCode));
        validateNotExpired(teamInvitation);

        Team team = teamInvitation.getTeam();
        boolean isJoined = teamMemberRepository.existsByTeamIdAndMemberId(team.getId(), memberId);
        return new InvitationJoinedResponse(team.getCode(), team.getName(), isJoined);
    }

    public String join(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository
                .findByCode(invitationCode)
                .orElseThrow(() -> new TeamInvitationNotFoundException(invitationCode));
        validateNotExpired(teamInvitation);

        Team team = teamInvitation.getTeam();
        validateNotJoined(team.getId(), memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        teamMemberRepository.save(
                TeamMember.builder()
                        .team(team)
                        .member(member)
                        .build()
        );
        return team.getCode();
    }

    private void validateNotExpired(TeamInvitation teamInvitation) {
        if (teamInvitation.isExpired()) {
            throw new ExpiredInvitationException();
        }
    }

    private void validateNotJoined(Long teamId, Long memberId) {
        if (teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new AlreadyJoinedTeamException();
        }
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> findTeams(Long memberId) {
        List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(memberId);
        return teamMembers.stream()
                .sorted(Comparator.comparingLong(TeamMember::getId))
                .map(TeamMember::getTeam)
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findMembersInTeam(Long memberId, String teamCode) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));
        validateJoined(team.getId(), memberId);

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(team.getId());
        return teamMembers.stream()
                .map(TeamMember::getMember)
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public void exitMemberFromTeam(Long memberId, String teamCode) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow(() -> new TeamNotFoundException(teamCode));

        TeamMember teamMember = teamMemberRepository
                .findByTeamIdAndMemberId(team.getId(), memberId)
                .orElseThrow(() -> new MismatchedTeamException(team.getId(), memberId));

        teamMemberRepository.delete(teamMember);
    }

    @Transactional(readOnly = true)
    public TeamResponse findDefaultTeam(Long memberId) {
        List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(memberId);
        return TeamResponse.from(findFirstTeamMember(teamMembers).getTeam());
    }

    private TeamMember findFirstTeamMember(List<TeamMember> teamMembers) {
        return teamMembers.stream()
                .min(Comparator.comparingLong(TeamMember::getId))
                .orElseThrow(() -> new ResourceNotFoundException("속해있는 팀이 없습니다."));
    }
}
