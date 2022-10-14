package com.morak.back.role.application;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.domain.RandomShuffleStrategy;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
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

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final RoleRepository roleRepository;

    // -- A



    // -- B



    // -- C
    public Long match(String teamCode, Long memberId) {
        // TODO: 2022/10/15 validate team and member
        List<Long> memberIds = findMemberIds(teamCode);
        Role role = findRoleByTeamCode(teamCode);
        RoleHistory roleHistory = role.matchMembers(memberIds, new RandomShuffleStrategy());
        return roleHistory.getId();
    }

    private List<Long> findMemberIds(String teamCode) {
        Team team = findTeamByCode(teamCode);
        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeam(team);
        return teamMembers.stream()
                .map(tm -> tm.getMember().getId())
                .collect(Collectors.toList());
    }

    private Team findTeamByCode(String teamCode) {
        return teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));
    }

    private Role findRoleByTeamCode(String teamCode) {
        return roleRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> RoleNotFoundException.ofTeam(CustomErrorCode.ROLE_NOT_FOUND_ERROR, teamCode));
    }

    // -- D


}
