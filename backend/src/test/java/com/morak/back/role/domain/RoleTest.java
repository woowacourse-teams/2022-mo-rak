package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void 생성_테스트() {
        // given
        RoleNames names = RoleNames.from(List.of("데일리 마스터", "서기"));

        // when
        Role role = new Role(names);

        // then
        assertThat(role).isNotNull();
    }

    @Test
    void 역할의_이름_목록을_변경한다() {
        // given
        Role role = new Role(RoleNames.from(List.of("데일리 마스터", "서기")));

        // when
        role.updateNames(RoleNames.from(List.of("카메라맨", "타임키퍼")));

        // then
        assertThat(role.getRoleNames().getValues()).containsExactly(
                new RoleName("카메라맨"),
                new RoleName("타임키퍼")
        );
    }
}
