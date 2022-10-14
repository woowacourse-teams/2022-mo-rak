package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class RoleServiceTest {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final RoleService roleService;

    @Autowired
    public RoleServiceTest(RoleRepository roleRepository,
                           MemberRepository memberRepository,
                           TeamRepository teamRepository,
                           TeamMemberRepository teamMemberRepository) {
        this.roleRepository = roleRepository;
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;

        this.roleService = new RoleService(roleRepository, memberRepository, teamRepository, teamMemberRepository);
    }

    // -- A

    // -- B

    @Test
    void 역할의_이름_목록을_수정한다() {
        // given
        String teamCode = "MoraK123";
        Long memberId = 1L;

        // when
        roleService.editRoleNames(teamCode, memberId, List.of("서기", "타임키퍼"));

        // then
        Role role = roleRepository.findByTeamCode(teamCode).orElseThrow();
        assertThat(role.getRoleNames().getValues()).containsExactly(new RoleName("서기"), new RoleName("타임키퍼"));
    }

    @Test
    void 역할정하기가_존재하지_않고_역할의_이름_목록을_수정하면_예외를_던진다() {
        // given
        String teamCode = "Betrayed";
        Long memberId = 4L;

        // when & then
        assertThatThrownBy(() -> roleService.editRoleNames(teamCode, memberId, List.of("서기", "타임키퍼")))
                .isInstanceOf(RoleNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.ROLE_NOT_FOUND_ERROR);
    }

    // -- C

    // -- D


}
