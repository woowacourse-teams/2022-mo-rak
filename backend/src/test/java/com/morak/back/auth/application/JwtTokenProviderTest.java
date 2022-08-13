package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.exception.AuthenticationException;
import com.morak.back.core.exception.CustomErrorCode;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String SECRET_KEY = "9875a0b4ee6605257509be56c0c0db8ac7657c56e008b2d0087efece6e0accd8";

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 3600000L);

    @Test
    void 토큰에서_payload를_가져온다() {
        // given
        String payload = "엘리";

        // when
        String token = jwtTokenProvider.createToken(payload);

        // then
        assertThat(jwtTokenProvider.parsePayload(token)).isEqualTo(payload);
    }

    @Test
    void 유효하지않은_토큰에서_payload를_가져올_때_예외를_던진다() {
        // given
        String invalidToken = "invalidToken";

        // when & then
        assertThatThrownBy(() ->jwtTokenProvider.parsePayload(invalidToken))
                .isInstanceOf(AuthenticationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR);
    }

    @Test
    void 유효하지않은_토큰인경우_예외를_던진다() {
        // given
        String token = "test-token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(AuthenticationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR);
    }

    @Test
    void 만료된_토큰인경우_예외를_던진다() {
        // given
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 0L);
        String token = jwtTokenProvider.createToken("엘리");

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(AuthenticationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.EXPIRED_AUTHORIZATION_ERROR);
    }
}
