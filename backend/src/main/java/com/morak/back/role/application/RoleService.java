package com.morak.back.role.application;

import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleDomainLogicException;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamNotFoundException;
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

    // -- B

    // -- C


    // -- D
    public RolesResponse findHistories(String teamCode) {
        teamRepository.findByCode(teamCode)
                .orElseThrow(() -> TeamNotFoundException.ofTeam(CustomErrorCode.TEAM_NOT_FOUND_ERROR, teamCode));

        // TODO: 2022/10/15 Exception 바꾸기
        Role role = roleRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new RoleDomainLogicException(CustomErrorCode.ROLE_NOT_FOUND_ERROR,
                        teamCode + " 팀의 역할이 존재하지 않습니다."));

        return RolesResponse.from(role.findAllGroupByDate());
    }
}
