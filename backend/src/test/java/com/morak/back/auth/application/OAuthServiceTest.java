package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.morak.back.auth.application.dto.OAuthAccessTokenResponse;
import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.auth.ui.dto.SigninResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    private static final String CODE = "test-code";
    private static final String ACCESS_TOKEN = "test-token";
    private static final Member MEMBER = Member.builder()
            .id(1L)
            .oauthId("oauthId")
            .name("박성우")
            .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
            .build();

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OAuthClient oAuthClient;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private OAuthService oAuthService;

    @BeforeEach
    void setUp() {
        given(oAuthClient.getAccessToken(anyString()))
                .willReturn(new OAuthAccessTokenResponse(ACCESS_TOKEN, null, null));
        given(oAuthClient.getMemberInfo(anyString()))
                .willReturn(new OAuthMemberInfoResponse(MEMBER.getOauthId(), MEMBER.getName(), MEMBER.getProfileUrl()));
    }

    @Test
    void OAuth_첫_로그인시_회원으로_등록하고_토큰을_발급한다() {
        // given
        given(memberRepository.findByOauthId(any())).willReturn(Optional.empty());
        given(memberRepository.save(any())).willReturn(MEMBER);
        given(tokenProvider.createToken(anyString())).willReturn(ACCESS_TOKEN);

        // when
        SigninResponse response = oAuthService.signin(new SigninRequest(CODE));

        // then
        assertThat(response.getToken()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    void OAuth_로그인시_토큰을_발급한다() {
        // given
        given(memberRepository.findByOauthId(any())).willReturn(Optional.of(MEMBER));
        given(tokenProvider.createToken(anyString())).willReturn(ACCESS_TOKEN);

        // when
        SigninResponse response = oAuthService.signin(new SigninRequest(CODE));

        // then
        assertThat(response.getToken()).isEqualTo(ACCESS_TOKEN);
    }
}
