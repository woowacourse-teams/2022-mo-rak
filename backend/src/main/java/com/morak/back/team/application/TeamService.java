package com.morak.back.team.application;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.application.AuthorizationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.domain.ExpiredTime;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamCreateEvent;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamDomainLogicException;
import com.morak.back.team.exception.TeamNotFoundException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    private final AuthorizationService authorizationService;

    private final SystemTime systemTime;

    public String createTeam(Long memberId, TeamCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team savedTeam = teamRepository.save(request.toTeam(CODE_GENERATOR));

        TeamMember teamMember = TeamMember.builder()
                .team(savedTeam)
                .member(member)
                .build();
        teamMemberRepository.save(teamMember);

        eventPublisher.publishEvent(new TeamCreateEvent(savedTeam.getCode()));
        return savedTeam.getCode();
    }

    public String createInvitationCode(Long memberId, String teamCode) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Team team = teamRepository.findByCode(teamCode).orElseThrow(
                            () -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
                    TeamInvitation savedTeamInvitation = teamInvitationRepository.save(
                            TeamInvitation.builder()
                                    .code(Code.generate(CODE_GENERATOR))
                                    .team(team) // TODO: 2022/10/26 얘 때문에 Team 찾음 
                                    .expiredAt(new ExpiredTime(systemTime))
                                    .build()
                    );
                    return savedTeamInvitation.getCode();
                }, teamCode, memberId
        );
    }

    @Transactional(readOnly = true)
    public InvitationJoinedResponse isJoined(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository
                .findByCode(invitationCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeamInvitation(
                                CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR,
                                invitationCode
                        )
                );
        validateNotExpired(teamInvitation);

        Team team = teamInvitation.getTeam();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));

        boolean isJoined = teamMemberRepository.existsByTeamAndMember(team, member);
        return new InvitationJoinedResponse(team.getCode(), team.getName(), isJoined);
    }

    public String join(Long memberId, String invitationCode) {
        TeamInvitation teamInvitation = teamInvitationRepository
                .findByCode(invitationCode)
                .orElseThrow(
                        () -> TeamNotFoundException.ofTeamInvitation(
                                CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR,
                                invitationCode
                        )
                );
        validateNotExpired(teamInvitation);

        Team team = teamInvitation.getTeam();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        validateNotJoined(team, member);

        teamMemberRepository.save(
                TeamMember.builder()
                        .team(team)
                        .member(member)
                        .build()
        );
        return team.getCode();
    }

    private void validateNotExpired(TeamInvitation teamInvitation) {
        if (teamInvitation.isExpired(systemTime)) {
            throw new TeamDomainLogicException(
                    CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR,
                    teamInvitation.getCode() + "는 만료된 초대코드입니다."
            );
        }
    }

    private void validateNotJoined(Team team, Member member) {
        if (teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw new TeamDomainLogicException(
                    CustomErrorCode.TEAM_ALREADY_JOINED_ERROR,
                    member.getId() + " 번의 멤버는 " + team.getId() + " 번의 그룹에 이미 속해있습니다."
            );
        }
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> findTeams(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));

        List<TeamMember> teamMembers = teamMemberRepository.findAllByMember(member);
        return teamMembers.stream()
                .sorted(Comparator.comparingLong(TeamMember::getId))
                .map(TeamMember::getTeam)
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findMembersInTeam(Long memberId, String teamCode) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamCode(teamCode);
                    Member member = memberRepository.findById(memberId).orElseThrow(
                            () -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
                    return teamMembers.stream()
                            .map(TeamMember::getMember)
                            .sorted(moveMeToFirst(member)) // TODO: 2022/10/26 얘 때문에 member 찾음 
                            .map(MemberResponse::from)
                            .collect(Collectors.toList());
                }, teamCode, memberId
        );
    }

    private Comparator<Member> moveMeToFirst(Member member) {
        return (Member m1, Member m2) -> {
            if (m1.equals(member)) {
                return -1;
            }
            return 1;
        };
    }

    public void exitMemberFromTeam(Long memberId, String teamCode) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    teamMemberRepository.deleteByTeamCodeAndMemberId(teamCode, memberId);
                    // TODO: 2022/10/26 DELETE 에서 조인 불가능
                    return null;
                }, teamCode, memberId
        );
    }

    @Transactional(readOnly = true)
    public TeamResponse findDefaultTeam(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));

        TeamMember teamMember = teamMemberRepository.findFirstByMemberOrderByIdAsc(member)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(
                                CustomErrorCode.TEAM_NOT_FOUND_ERROR, memberId + "번의 멤버가 속해있는 팀이 없습니다."
                        )
                );
        return TeamResponse.from(teamMember.getTeam());
    }
}
