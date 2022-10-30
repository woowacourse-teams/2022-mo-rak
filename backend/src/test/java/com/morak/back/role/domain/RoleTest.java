package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void 생성_테스트() {
        // given & when
        Role role = new Role("TestTeam");

        // then
        assertThat(role.getRoleNames().getValues()).hasSize(1);
    }

    @Test
    void 역할의_이름_목록을_변경한다() {
        // given
        Role role = new Role("TestTeam");

        // when
        role.updateNames(List.of("카메라맨", "타임키퍼"));

        // then
        assertThat(role.getRoleNames().getValues()).containsExactly(
                new RoleName("카메라맨"),
                new RoleName("타임키퍼")
        );
    }

    @Test
    void 역할의_이름은_중복될_수_있다() {
        // given
        Role role = new Role("TestTeam");

        // when
        role.updateNames(List.of("카메라맨", "타임키퍼", "타임키퍼"));

        // then
        assertThat(role.getRoleNames().getValues()).containsExactly(
                new RoleName("카메라맨"),
                new RoleName("타임키퍼"),
                new RoleName("타임키퍼")
        );
    }

    @Test
    void 역할을_매칭한다() {
        // given
        Role role = new Role("TestTeam");
        role.updateNames(List.of("데일리 마스터", "서기"));
        List<Long> memberIds = Arrays.asList(1L, 2L, 3L);

        // when
        RoleHistory actual = role.matchMembers(memberIds, ids -> {
        });

        // then
        Assertions.assertAll(
                () -> assertThat(actual.getDateTime()).isCloseTo(
                        LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)
                ),
                () -> assertThat(actual.getMatchResults()).hasSize(2),
                () -> assertThat(actual.getMatchResults().get(0).getRoleName()).isEqualTo(new RoleName("데일리 마스터")),
                () -> assertThat(actual.getMatchResults().get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(actual.getMatchResults().get(1).getRoleName()).isEqualTo(new RoleName("서기")),
                () -> assertThat(actual.getMatchResults().get(1).getMemberId()).isEqualTo(2L)
        );
    }

    @Test
    void 중복된_역할이_있을_때_역할을_매칭한다() {
        // given
        Role role = new Role("TestTeam");
        role.updateNames(List.of("데일리 마스터", "서기", "서기"));
        List<Long> memberIds = Arrays.asList(1L, 2L, 3L);

        // when
        RoleHistory actual = role.matchMembers(memberIds, ids -> {
        });

        // then
        Assertions.assertAll(
                () -> assertThat(actual.getDateTime()).isCloseTo(
                        LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)
                ),
                () -> assertThat(actual.getMatchResults()).hasSize(3),
                () -> assertThat(actual.getMatchResults().get(0).getRoleName()).isEqualTo(new RoleName("데일리 마스터")),
                () -> assertThat(actual.getMatchResults().get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(actual.getMatchResults().get(1).getRoleName()).isEqualTo(new RoleName("서기")),
                () -> assertThat(actual.getMatchResults().get(1).getMemberId()).isEqualTo(2L),
                () -> assertThat(actual.getMatchResults().get(2).getRoleName()).isEqualTo(new RoleName("서기")),
                () -> assertThat(actual.getMatchResults().get(2).getMemberId()).isEqualTo(3L)
        );
    }

    @Test
    void 역할을_정할때_멤버의_수보다_역할의_개수가_많으면_예외를_던진다() {
        // given
        Role role = new Role("TestTeam");
        role.updateNames(List.of("데일리 마스터", "서기"));
        List<Long> memberIds = Arrays.asList(1L);

        // when & then
        assertThatThrownBy(() -> role.matchMembers(memberIds, ids -> {
        }))
                .isInstanceOf(RoleDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.ROLE_NAMES_MAX_SIZE_ERROR);
    }
}
