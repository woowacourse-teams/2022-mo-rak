package com.morak.back.role.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleHistories;
import com.morak.back.role.domain.RoleHistory;
import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.role.exception.RoleDomainLogicException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
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

        // when

        // then
    }

    // -- C


    // -- D
    @Test
    void 역할_히스토리_목록을_조회한다() {
        // given
        Role role = getRole();
        roleRepository.save(role);
        roleRepository.flush();

        // when
        RolesResponse rolesResponse = roleService.findHistories("MoraK123");

        // then
        Assertions.assertAll(
                () -> assertThat(rolesResponse.getRoles()).hasSize(2),
                () -> assertThat(rolesResponse.getRoles()).extracting("date", "role")
                        .containsExactly(
                                tuple(LocalDate.of(2022, 10, 14),
                                        List.of(RoleResponse.from(Map.entry("서기", 2L)))),
                                tuple(LocalDate.of(2022, 10, 13),
                                        List.of(RoleResponse.from(Map.entry("서기", 1L))))
                        )
        );
    }

    private Role getRole() {
        String 데일리_마스터 = "데일리 마스터";
        String 서기 = "서기";
        LocalDateTime now = LocalDateTime.of(2022, 10, 14, 22, 52);
        RoleName Roll_데일리_마스터 = new RoleName(데일리_마스터);
        RoleName Roll_서기 = new RoleName(서기);
        RoleHistories roleHistories = new RoleHistories();

        RoleHistory history1 = new RoleHistory(now, Map.of(Roll_데일리_마스터, 1L));
        RoleHistory history2 = new RoleHistory(now.plusSeconds(10), Map.of(Roll_서기, 2L));
        RoleHistory history3 = new RoleHistory(now.minusDays(1), Map.of(Roll_데일리_마스터, 2L));
        RoleHistory history4 = new RoleHistory(now.minusDays(1).plusSeconds(10), Map.of(Roll_서기, 1L));

        roleHistories.add(history1);
        roleHistories.add(history2);
        roleHistories.add(history3);
        roleHistories.add(history4);
        Role role = new Role("MoraK123", RoleNames.from(List.of(데일리_마스터, 서기)), roleHistories);
        return role;
    }

    @Test
    void 팀에_역할이_존재하지_않는_경우_예외를_던진다() {
        // given
        String teamCode = "MoraK123";

        // TODO: 2022/10/15 Exception 변경
        // when & then
        assertThatThrownBy(() -> roleService.findHistories(teamCode))
                .isInstanceOf(RoleDomainLogicException.class);
    }
}
