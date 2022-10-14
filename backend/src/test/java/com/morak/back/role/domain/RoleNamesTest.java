package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.util.List;
import org.junit.jupiter.api.Test;

class RoleNamesTest {

    @Test
    void 생성_테스트() {
        // given
        List<RoleName> names = List.of(new RoleName("데일리 마스터"));

        // when
        RoleNames roleNames = new RoleNames(names);

        // then
        assertThat(roleNames).isNotNull();
    }

    @Test
    void 역할의_개수는_하나_이상이어야_한다() {
        // given
        List<RoleName> names = List.of();

        // when & then
        assertThatThrownBy(() -> new RoleNames(names))
                .isInstanceOf(RoleDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.INVALID_ROLE_NAMES_SIZE_ERROR);
    }
}
