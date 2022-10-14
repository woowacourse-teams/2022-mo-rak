package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.*;

import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@ServiceTest
class RoleServiceTest {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final RoleService roleService;

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

        // when

        // then
    }


    // -- C



    // -- D


}
