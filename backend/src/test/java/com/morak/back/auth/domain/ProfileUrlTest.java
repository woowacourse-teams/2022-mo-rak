package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import org.junit.jupiter.api.Test;

class ProfileUrlTest {

    @Test
    void ProfileUrl를_생성할_수_있다() {
        // given
        String value = "https://avatars.githubusercontent.com/u/79205414?v=4";

        // when
        ProfileUrl profileUrl = new ProfileUrl(value);

        // then
        assertThat(profileUrl.getValue()).isEqualTo(value);
    }

    @Test
    void ProfileUrl가_공백인_경우_예외를_던진다() {
        // given
        String value = "  ";

        // when & then
        assertThatThrownBy(() -> new ProfileUrl(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.PROFILE_URL_BLANK_ERROR);
    }

    @Test
    void ProfileUrl의_길이가_1_미만인_경우_예외를_던진다() {
        // given
        String value = "";

        // when & then
        assertThatThrownBy(() -> new ProfileUrl(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.PROFILE_URL_BLANK_ERROR);
    }

    @Test
    void ProfileUrl의_길이가_255_초과인_경우_예외를_던진다() {
        // given
        String value = "https://avatars.githubusercontent.com/u/79205414?v=4" + "o".repeat(204);

        // when & then
        assertThatThrownBy(() -> new ProfileUrl(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.PROFILE_URL_LENGTH_ERROR);
    }

    @Test
    void ProfileUrl이_http로_시작하지않으면_예외를_던진다() {
        // given
        String value = "htt://";

        // when & then
        assertThatThrownBy(() -> new ProfileUrl(value))
                .isInstanceOf(MemberDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.PROFILE_URL_PATTERN_ERROR);
    }
}
