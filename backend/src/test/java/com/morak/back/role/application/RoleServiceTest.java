package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RoleResponse;
import com.morak.back.role.application.dto.RolesResponse;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleNotFoundException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Test
    void 역할_이름_목록을_조회한다() {
        // given
        roleRepository.save(
                new Role(
                        team.getCode(),
                        new RoleNames(List.of(new RoleName("반장"), new RoleName("부반장"))),
                        new RoleHistories()
                )
        );

        // when
        RoleNameResponses roleNameResponses = roleService.findRoleNames(team.getCode(), member.getId());

        // then
        assertThat(roleNameResponses.getRoles()).containsExactly("반장", "부반장");
    }

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
    @Test
    void 역할_히스토리_목록을_조회한다() {
        // given
        Role role = getRole();
        roleRepository.save(role);
        roleRepository.flush();

        // when
        RolesResponse rolesResponse = roleService.findHistories(team.getCode(), member.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(rolesResponse.getRoles()).hasSize(2),
                () -> assertThat(rolesResponse.getRoles()).extracting("date", "role")
                        .containsExactly(
                                tuple(LocalDate.of(2022, 10, 14),
                                        List.of(RoleResponse.from(Map.entry("서기", member.getId())))),
                                tuple(LocalDate.of(2022, 10, 13),
                                        List.of(RoleResponse.from(Map.entry("서기", member.getId()))))
                        )
        );
    }

    private Role getRole() {
        String 데일리_마스터 = "데일리 마스터";
        String 서기 = "서기";
        LocalDateTime now = LocalDateTime.of(2022, 10, 14, 22, 52);
        RoleName Role_데일리_마스터 = new RoleName(데일리_마스터);
        RoleName Role_서기 = new RoleName(서기);
        RoleHistories roleHistories = new RoleHistories();

        Long memberId = member.getId();
        RoleHistory history1 = new RoleHistory(now, Map.of(Role_데일리_마스터, memberId));
        RoleHistory history2 = new RoleHistory(now.plusSeconds(10), Map.of(Role_서기, memberId));
        RoleHistory history3 = new RoleHistory(now.minusDays(1), Map.of(Role_데일리_마스터, memberId));
        RoleHistory history4 = new RoleHistory(now.minusDays(1).plusSeconds(10), Map.of(Role_서기, memberId));

        roleHistories.add(history1);
        roleHistories.add(history2);
        roleHistories.add(history3);
        roleHistories.add(history4);
        return new Role(team.getCode(), RoleNames.from(List.of(데일리_마스터, 서기)), roleHistories);
    }

    @Test
    void 팀에_역할이_존재하지_않는_경우_예외를_던진다() {
        // given
        String teamCode = team.getCode();

        // when & then
        assertThatThrownBy(() -> roleService.findHistories(teamCode, member.getId()))
                .isInstanceOf(RoleNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.ROLE_NOT_FOUND_ERROR);
    }
}
