package com.morak.back.team.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamMember;
import com.morak.back.auth.domain.TeamMemberRepository;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.time.LocalDateTime;
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

    private static final int TEAM_CODE_LENGTH = 8;
    private static final int INVITATION_CODE_LENGTH = 10;
    private static final long EXPIRED_MINUTES = 30L;

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    public String createTeam(Long memberId, TeamCreateRequest request) {
        Team team = new Team(null, request.getName(), CodeGenerator.createRandomCode(TEAM_CODE_LENGTH));
        Member member = memberRepository.findById(memberId).orElseThrow();
        Team savedTeam = teamRepository.save(team);

        TeamMember teamMember = new TeamMember(null, savedTeam, member);
        teamMemberRepository.save(teamMember);

        return savedTeam.getCode();
    }

    public String createInvitationCode(Long memberId, String teamCode) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow();
        boolean isExist = teamMemberRepository.existsByTeamIdAndMemberId(team.getId(), memberId);

        if (!isExist) {
            throw new MismatchedTeamException("팀에 속해있지 않습니다.");
        }

        TeamInvitation teamInvitation = new TeamInvitation(
                null,
                team,
                CodeGenerator.createRandomCode(INVITATION_CODE_LENGTH),
                LocalDateTime.now().plusMinutes(EXPIRED_MINUTES)
        );

        TeamInvitation savedTeamInvitation = teamInvitationRepository.save(teamInvitation);

        return savedTeamInvitation.getCode();
    }

    @Transactional(readOnly = true)
    public InvitationJoinedResponse isJoined(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository.findByCode(invitationCode).orElseThrow();
        validateNotExpired(teamInvitation);
        Team team = teamInvitation.getTeam();
        boolean isJoined = teamMemberRepository.existsByTeamIdAndMemberId(team.getId(), memberId);
        return new InvitationJoinedResponse(team.getCode(), team.getName(), isJoined);
    }

    public String join(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository.findByCode(invitationCode).orElseThrow();
        validateNotExpired(teamInvitation);
        Team team = teamInvitation.getTeam();
        boolean isJoined = teamMemberRepository.existsByTeamIdAndMemberId(team.getId(), memberId);
        if (isJoined) {
            throw new AlreadyJoinedTeamException("팀에 이미 속해있습니다.");
        }
        Member member = memberRepository.findById(memberId).orElseThrow();
        teamMemberRepository.save(new TeamMember(null, team, member));
        return team.getCode();
    }

    private void validateNotExpired(TeamInvitation teamInvitation) {
        if (teamInvitation.isExpired()) {
            throw new ExpiredInvitationException("이미 만료된 초대 코드입니다.");
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
        Team team = teamRepository.findByCode(teamCode).orElseThrow();
        if (!teamMemberRepository.existsByTeamIdAndMemberId(team.getId(), memberId)) {
            throw new MismatchedTeamException("팀에 속해있지 않습니다.");
        }

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(team.getId());
        return teamMembers.stream()
                .map(TeamMember::getMember)
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public void exitMemberInTeam(Long memberId, String teamCode) {
        Team team = teamRepository.findByCode(teamCode).orElseThrow();
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(team.getId(), memberId)
                .orElseThrow(() -> new MismatchedTeamException("팀에 속해있지 않습니다."));
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
                .orElseThrow(() -> new MismatchedTeamException("속해있는 팀이 없습니다."));
    }

}
