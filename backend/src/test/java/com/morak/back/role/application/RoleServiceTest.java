package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.util.List;
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

        roleService = new RoleService(teamRepository, teamMemberRepository, roleRepository, memberRepository);
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
        teamMemberRepository.save(TeamMember.builder().team(team).member(member).build());
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
