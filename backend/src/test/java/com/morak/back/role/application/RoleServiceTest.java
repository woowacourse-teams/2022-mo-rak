package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class RoleServiceTest {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final RoleRepository roleRepository;

    private final RoleService roleService;

    private Member member;
    private Team team;

    @Autowired
    public RoleServiceTest(MemberRepository memberRepository, TeamRepository teamRepository,
                           TeamMemberRepository teamMemberRepository, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.roleRepository = roleRepository;
        this.roleService = new RoleService(teamRepository, teamMemberRepository, roleRepository);
    }

    @BeforeEach
    void setup() {
        member = memberRepository.save(Member.builder()
                .oauthId("oauthmem")
                .name("박성우")
                .profileUrl("http://park-profile.com")
                .build());

        team = teamRepository.save(Team.builder()
                .name("team")
                .code(Code.generate(length -> "abcd1234"))
                .build());

        teamMemberRepository.save(new TeamMember(null, team, member));
    }

    // -- A



    // -- B



    // -- C
    @Test
    void 역할을_매칭한다() {
        // given
        Member ellie = memberRepository.save(Member.builder()
                .oauthId("ellie-oauth-id")
                .name("한해리")
                .profileUrl("http://ellie-profile.com")
                .build());
        teamMemberRepository.save(new TeamMember(null, team, ellie));

        Role role = new Role(team.getCode());
        roleRepository.save(role);
        int beforeSize = role.getRoleHistories().getValues().size();

        // when
        roleService.match(team.getCode(), member.getId());

        roleRepository.flush(); // 테스트에서는 flush()를 해야 history의 id 값을 얻어올 수 있다.

        // then
        RoleHistories afterHistories = role.getRoleHistories();
        Assertions.assertAll(
                () -> assertThat(afterHistories.getValues().get(0).getId()).isNotNull(),
                () -> assertThat(afterHistories.getValues().size()).isGreaterThan(beforeSize)
        );
    }

    @Test
    void 역할을_매칭하는데_역할이_존재하지_않을_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> roleService.match(team.getCode(), member.getId()))
                .isInstanceOf(RoleNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.ROLE_NOT_FOUND_ERROR);
    }


    // -- D


}
