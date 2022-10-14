package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.*;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    void 역할_이름_목록을_조회한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .oauthId("oauthmem")
                .name("박성우")
                .profileUrl("http://park-profile.com")
                .build());

        Team team = teamRepository.save(Team.builder()
                .name("team")
                .code(Code.generate(length -> "abcd1234"))
                .build());

        roleRepository.save(new Role(team.getCode(), new RoleNames(List.of(new RoleName("반장"), new RoleName("부반장"))), new RoleHistories()));
        // when
        RoleNameResponses roleNameResponses = roleService.findRoleNames(team.getCode(), member.getId());

        // then
        assertThat(roleNameResponses.getRoles()).containsExactly("반장", "부반장");
    }


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
