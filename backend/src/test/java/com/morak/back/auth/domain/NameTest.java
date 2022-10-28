package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    void MemberName를_생성할_수_있다() {
        // given
        String value = "멤버 이름";

        // when
        Name name = new Name(value);

        // then
        assertThat(name.getValue()).isEqualTo(value);
    }

    @Test
    void MemberName가_공백인_경우_예외를_던진다() {
        // given
        String value = "  ";

        // when & then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NAME_BLANK_ERROR);
    }

    @Test
    void MemberName의_길이가_1_미만인_경우_예외를_던진다() {
        // given
        String value = "";

        // when & then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NAME_BLANK_ERROR);
    }

    @Test
    void OAuthId의_길이가_255_초과인_경우_예외를_던진다() {
        // given
        String value = "o".repeat(256);

        // when & then
        assertThatThrownBy(() -> new OAuthId(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.OAUTH_ID_LENGTH_ERROR);
    }
}
