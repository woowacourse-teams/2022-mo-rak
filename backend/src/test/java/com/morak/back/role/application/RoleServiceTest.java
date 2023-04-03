package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.application.dto.HistoryResponse;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RoleResponse;
import com.morak.back.role.application.dto.RolesResponse;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleMatchResult;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleDomainLogicException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@ServiceTest
class RoleServiceTest {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final RoleRepository roleRepository;

    private final RoleService roleService;

    @Autowired
    public RoleServiceTest(
            MemberRepository memberRepository,
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            RoleRepository roleRepository,
            RoleService roleService
    ) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    private Member member;
    private Team team;

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
    void 역할_이름_목록을_조회할_때_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member otherMember = saveOtherMember();

        roleRepository.save(new Role(team.getCode()));

        // when & then
        assertThatThrownBy(() -> roleService.editRoleNames(team.getCode(), otherMember.getId(), List.of("서기", "타임키퍼")))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 역할_이름_목록을_조회할_때_멤버가_없는_경우_예외를_던진다() {
        // given
        roleRepository.save(new Role(team.getCode()));

        // when & then
        assertThatThrownBy(() -> roleService.editRoleNames(team.getCode(), 100L, List.of("서기", "타임키퍼")))
                .isInstanceOf(MemberNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    void 역할_이름_목록을_조회할_때_팀이_없는_경우_예외를_던진다() {
        // given
        roleRepository.save(new Role(team.getCode()));

        // when & then
        assertThatThrownBy(() -> roleService.editRoleNames("123456ll", member.getId(), List.of("서기", "타임키퍼")))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 역할의_이름_목록을_수정한다() {
        // given
        roleRepository.save(new Role(team.getCode()));

        // when
        roleService.editRoleNames(team.getCode(), member.getId(), List.of("서기", "타임키퍼"));

        // then
        Role role = roleRepository.findByTeamCode(team.getCode()).orElseThrow();
        assertThat(role.getRoleNames().getValues()).containsExactly(
                new RoleName("서기"),
                new RoleName("타임키퍼")
        );
    }

    @Test
    void 역할의_이름_목록을_수정할_때_멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member otherMember = saveOtherMember();

        roleRepository.save(new Role(team.getCode()));

        // when & then
        assertThatThrownBy(() -> roleService.editRoleNames(team.getCode(), otherMember.getId(), List.of("서기", "타임키퍼")))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 중복된_이름으로_역할의_이름_목록을_수정한다() {
        // given
        roleRepository.save(new Role(team.getCode()));

        // when
        roleService.editRoleNames(team.getCode(), member.getId(), List.of("서기", "타임키퍼", "타임키퍼"));

        // then
        Role role = roleRepository.findByTeamCode(team.getCode()).orElseThrow();
        assertThat(role.getRoleNames().getValues()).containsExactly(
                new RoleName("서기"),
                new RoleName("타임키퍼"),
                new RoleName("타임키퍼")
        );
    }

    @Test
    void 역할을_매칭한다(@Autowired EntityManager entityManager) {
        // given
        Member otherMember = saveOtherMember();
        teamMemberRepository.save(new TeamMember(null, team, otherMember));

        Role role = new Role(team.getCode());
        roleRepository.save(role);
        int beforeSize = role.getRoleHistories().getValues().size();

        // when
        roleService.matchRoleAndMember(team.getCode(), member.getId());

        entityManager.flush(); // 테스트에서는 flush()를 해야 history의 id 값을 얻어올 수 있다.

        // then
        RoleHistories afterHistories = role.getRoleHistories();
        Assertions.assertAll(
                () -> assertThat(afterHistories.getValues().get(0).getId()).isNotNull(),
                () -> assertThat(afterHistories.getValues().size()).isGreaterThan(beforeSize)
        );
    }

    @Test
    void 중복된_역할이_있을_때_역할을_매칭한다(@Autowired EntityManager entityManager) {
        // given
        Member otherMember = saveOtherMember();
        teamMemberRepository.save(new TeamMember(null, team, otherMember));

        Role role = new Role(team.getCode());
        roleRepository.save(role);
        roleService.editRoleNames(team.getCode(), member.getId(), List.of("엘사모", "엘사모"));
        int beforeSize = role.getRoleHistories().getValues().size();

        // when
        roleService.matchRoleAndMember(team.getCode(), member.getId());

        entityManager.flush();
        entityManager.clear();

        // then
        RoleHistories afterHistories = roleRepository.findByTeamCode(team.getCode()).orElseThrow().getRoleHistories();
        Assertions.assertAll(
                () -> assertThat(afterHistories.getValues().get(0).getId()).isNotNull(),
                () -> assertThat(afterHistories.getValues().size()).isGreaterThan(beforeSize)
        );
    }

    @Test
    void 역할을_매칭하는데_역할이_멤버보다_많을_경우_예외를_던진다() {
        // given
        roleRepository.save(new Role(team.getCode(), RoleNames.from(List.of("서기", "타임키퍼")), new RoleHistories()));

        // when & then
        assertThatThrownBy(() -> roleService.matchRoleAndMember(team.getCode(), member.getId()))
                .isInstanceOf(RoleDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.ROLE_NAMES_MAX_SIZE_ERROR);
    }

    @Test
    void 역할_히스토리_목록을_조회한다(@Autowired EntityManager entityManager) {
        // given
        saveRoleWithHistories();
        entityManager.flush();
        entityManager.clear();

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

    private Role saveRoleWithHistories() {
        String 데일리_마스터 = "데일리 마스터";
        String 서기 = "서기";
        LocalDateTime now = LocalDateTime.of(2022, 10, 14, 22, 52);
        RoleName Role_데일리_마스터 = new RoleName(데일리_마스터);
        RoleName Role_서기 = new RoleName(서기);
        RoleHistories roleHistories = new RoleHistories();

        Long memberId = member.getId();
        RoleHistory history1 = new RoleHistory(now, List.of(new RoleMatchResult(Role_데일리_마스터, memberId)), null);
        RoleHistory history2 = new RoleHistory(now.plusSeconds(10), List.of(new RoleMatchResult(Role_서기, memberId)),
                null);
        RoleHistory history3 = new RoleHistory(now.minusDays(1), List.of(new RoleMatchResult(Role_데일리_마스터, memberId)),
                null);
        RoleHistory history4 = new RoleHistory(now.minusDays(1).plusSeconds(10),
                List.of(new RoleMatchResult(Role_서기, memberId)), null);

        roleHistories.add(history1);
        roleHistories.add(history2);
        roleHistories.add(history3);
        roleHistories.add(history4);

        return roleRepository.save(new Role(team.getCode(), RoleNames.from(List.of(데일리_마스터, 서기)), roleHistories));
    }

    private Member saveOtherMember() {
        return memberRepository.save(Member.builder()
                .oauthId("ellie-oauth-id")
                .name("한해리")
                .profileUrl("http://ellie-profile.com")
                .build());
    }
}
