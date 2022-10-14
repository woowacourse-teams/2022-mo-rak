package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import org.junit.jupiter.api.Test;

class RoleNameTest {

    @Test
    void 생성_테스트() {
        // given
        String name = "hello";

        // when
        RoleName roleName = new RoleName(name);

        // then
        assertThat(roleName).isNotNull();
    }

    @Test
    void 역할의_이름은_20자를_넘을수_없다() {
        // given
        String name = "밥".repeat(21);

        // when & then
        assertThatThrownBy(() -> new RoleName(name))
                .isInstanceOf(RoleDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.INVALID_ROLE_NAME_LENGTH_ERROR);
    }
}
