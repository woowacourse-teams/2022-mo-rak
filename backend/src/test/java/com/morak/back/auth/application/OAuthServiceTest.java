package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.morak.back.auth.application.dto.OAuthAccessTokenResponse;
import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;
import com.morak.back.auth.domain.Member2;
import com.morak.back.auth.domain.Member2Repository;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.auth.ui.dto.SigninResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    private static final String CODE = "test-code";
    private static final String ACCESS_TOKEN = "test-token";

    @Mock
    private Member2Repository member2Repository;

    @Mock
    private OAuthClient oAuthClient;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private OAuthService oAuthService;

    @Test
    public void OAuth_첫_로그인시_회원으로_등록하고_토큰을_발급한다() {
        // given
        given(oAuthClient.getAccessToken(anyString())).willReturn(new OAuthAccessTokenResponse(ACCESS_TOKEN, null, null));
        given(oAuthClient.getMemberInfo(anyString())).willReturn(new OAuthMemberInfoResponse());
        given(member2Repository.findByOauthId(any())).willReturn(Optional.empty());
        given(member2Repository.save(any())).willReturn(new Member2(1L, null, null, null));
        given(tokenProvider.createToken(anyString())).willReturn(ACCESS_TOKEN);

        // when
        SigninResponse response = oAuthService.signin(new SigninRequest(CODE));

        // then
        assertThat(response.getToken()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void OAuth_로그인시_토큰을_발급한다() {
        // given
        given(oAuthClient.getAccessToken(anyString())).willReturn(new OAuthAccessTokenResponse(ACCESS_TOKEN, null, null));
        given(oAuthClient.getMemberInfo(anyString())).willReturn(new OAuthMemberInfoResponse());
        given(member2Repository.findByOauthId(any())).willReturn(Optional.of(new Member2(1L, null, null, null)));
        given(tokenProvider.createToken(anyString())).willReturn(ACCESS_TOKEN);

        // when
        SigninResponse response = oAuthService.signin(new SigninRequest(CODE));

        // then
        assertThat(response.getToken()).isEqualTo(ACCESS_TOKEN);
    }
}
