package com.morak.back.team.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamMember;
import com.morak.back.auth.domain.TeamMemberRepository;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.exception.TeamMismatchException;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import java.time.LocalDateTime;
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
            throw new TeamMismatchException("팀에 속해있지 않습니다.");
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
}
