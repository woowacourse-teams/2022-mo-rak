package com.morak.back.role.application;

import com.morak.back.core.application.AuthorizationService;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RolesResponse;
import com.morak.back.role.domain.RandomShuffleStrategy;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.team.domain.TeamCreateEvent;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final TeamMemberRepository teamMemberRepository;
    private final RoleRepository roleRepository;

    private final AuthorizationService authorizationService;

    public RoleNameResponses findRoleNames(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Role role = findRoleByTeamCode(teamCode);
                    return RoleNameResponses.from(role.getRoleNames());
                }, teamCode, memberId
        );
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultRole(TeamCreateEvent event) {
        roleRepository.save(new Role(event.getTeamCode()));
    }

    public void editRoleNames(String teamCode, Long memberId, List<String> names) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    Role role = findRoleByTeamCode(teamCode);
                    role.updateNames(names);
                    return null;
                }, teamCode, memberId
        );
    }

    public Long matchRoleAndMember(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    List<Long> memberIds = findMemberIds(teamCode);
                    Role role = findRoleByTeamCode(teamCode);
                    RoleHistory roleHistory = role.matchMembers(memberIds, new RandomShuffleStrategy());
                    roleRepository.save(role);
                    return roleHistory.getId();
                }, teamCode, memberId
        );
    }

    private List<Long> findMemberIds(String teamCode) {
        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamCode(teamCode);
        return teamMembers.stream()
                .map(teamMember -> teamMember.getMember().getId())
                .collect(Collectors.toList());
    }

    public RolesResponse findHistories(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Role role = findRoleByTeamCode(teamCode);
                    return RolesResponse.from(role.findAllGroupByDate());
                }, teamCode, memberId
        );
    }

    private Role findRoleByTeamCode(String teamCode) {
        return roleRepository.findByTeamCode(teamCode).orElseThrow(() -> new RoleNotFoundException(
                CustomErrorCode.ROLE_NOT_FOUND_ERROR,
                teamCode + " 의 팀 코드에 해당하는 역할정하기를 찾을 수 없습니다"
        ));
    }
}
