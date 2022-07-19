package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.exception.AuthorizationException;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String SECRET_KEY = "9875a0b4ee6605257509be56c0c0db8ac7657c56e008b2d0087efece6e0accd8";

    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void 토큰에서_payload를_가져온다() {
        // given
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 3600000L);
        String payload = "엘리";

        // when
        String token = jwtTokenProvider.createToken(payload);

        // then
        assertThat(jwtTokenProvider.parsePayload(token)).isEqualTo(payload);
    }

    @Test
    public void 유효하지않은_토큰인경우_예외를_던진다() {
        // given
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 3600000L);
        String token = "test-token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    public void 만료된_토큰인경우_예외를_던진다() {
        // given
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 0L);
        String token = jwtTokenProvider.createToken("엘리");

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(AuthorizationException.class);
    }
}
