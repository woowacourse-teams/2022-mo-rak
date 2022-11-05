package com.morak.back.role.application;

import com.morak.back.core.application.AuthorizationService;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RolesResponse;
import com.morak.back.role.domain.RandomShuffleStrategy;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleRepository;
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

    private final RoleRepository roleRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final AuthorizationService authorizationService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createDefaultRole(TeamCreateEvent event) {
        roleRepository.save(new Role(event.getTeamCode()));
    }

    public RoleNameResponses findRoleNames(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Role role = getRoleByTeamCode(teamCode);
                    return RoleNameResponses.from(role.getRoleNames());
                }, teamCode, memberId
        );
    }

    public void editRoleNames(String teamCode, Long memberId, List<String> names) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    Role role = getRoleByTeamCode(teamCode);
                    role.updateNames(names);
                    return null;
                }, teamCode, memberId
        );
    }

    public Long matchRoleAndMember(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    List<Long> memberIds = findMemberIds(teamCode);
                    Role role = getWithOnlyHistoriesPerDate(teamCode);
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
                    Role role = getWithOnlyHistoriesPerDate(teamCode);
                    return RolesResponse.from(role.findAllGroupByDate());
                }, teamCode, memberId
        );
    }

    private Role getRoleByTeamCode(String teamCode) {
        return roleRepository.findByTeamCode(teamCode)
                .orElseGet(() -> roleRepository.save(new Role(teamCode)));
    }

    private Role getWithOnlyHistoriesPerDate(String teamCode) {
        return roleRepository.findWithOnlyHistoriesPerDate(teamCode)
                .orElseGet(() -> roleRepository.save(new Role(teamCode)));
    }
}
