package com.morak.back.role.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    // -- A
    public RoleNameResponses findRoleNames(String teamCode, Long memberId) {
        Role role = roleRepository.findByTeamCode(teamCode).orElseThrow();
        return new RoleNameResponses(role.getRoleNames().getValues().stream()
                .map(RoleName::getValue)
                .collect(Collectors.toList()));
    }


    // -- B

    public void editRoleNames(String teamCode, Long memberId, List<String> names) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.of(CustomErrorCode.MEMBER_NOT_FOUND_ERROR, memberId));
        Team team = teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
        validateMemberInTeam(team, member);

        Role role = roleRepository.findByTeamCode(teamCode).orElseThrow();
        role.updateNames(names);
    }

    private void validateMemberInTeam(Team team, Member member) {
        if (!teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw TeamAuthorizationException.of(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR, team.getId(),
                    member.getId());
        }
    }

    // -- C



    // -- D


}
