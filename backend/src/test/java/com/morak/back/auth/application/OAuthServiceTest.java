package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.ui.dto.ChangeNameRequest;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.auth.ui.dto.SigninResponse;
import com.morak.back.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OAuthServiceTest {

    private static final String CODE = "test-code";

    private final OAuthClient oAuthClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final OAuthService oAuthService;

    @Autowired
    public OAuthServiceTest(
            OAuthClient oAuthClient,
            TokenProvider tokenProvider,
            MemberRepository memberRepository,
            OAuthService oAuthService
    ) {
        this.oAuthClient = oAuthClient;
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
        this.oAuthService = oAuthService;
    }

    @Test
    void OAuth_첫_로그인시_회원으로_등록하고_토큰을_발급한다() {
        // given
        SigninRequest request = new SigninRequest(CODE);

        // when
        SigninResponse response = oAuthService.signin(request);
        String payload = tokenProvider.parsePayload(response.getToken());
        Member savedMember = memberRepository.findById(Long.parseLong(payload)).orElseThrow();

        // then
        OAuthMemberInfoResponse memberResponse = oAuthClient.getMemberInfo("ignored");
        assertThat(savedMember).extracting("oauthId", "name", "profileUrl")
                .containsExactly(memberResponse.getOauthId(), memberResponse.getName(), memberResponse.getProfileUrl());
    }

    @Test
    void OAuth_로그인시_토큰을_발급한다() {
        // given
        Member savedMember = memberRepository.save(oAuthClient.getMemberInfo("ignored").toMember());

        // when
        SigninResponse response = oAuthService.signin(new SigninRequest(CODE));
        String payload = tokenProvider.parsePayload(response.getToken());
        Member member = memberRepository.findById(Long.parseLong(payload)).orElseThrow();

        // then
        assertThat(member).isEqualTo(savedMember);
    }

    @Test
    void 자기자신을_조회한다() {
        // given
        Member savedMember = memberRepository.save(oAuthClient.getMemberInfo("ignored").toMember());

        // when
        MemberResponse response = oAuthService.findMember(savedMember.getId());

        // then
        assertThat(savedMember.getId()).isEqualTo(response.getId());
    }

    @Test
    void 본인의_이름을_변경한다() {
        // given
        Member savedMember = memberRepository.save(oAuthClient.getMemberInfo("ignored").toMember());

        ChangeNameRequest request = new ChangeNameRequest("변경하려는 이름");

        // when
        oAuthService.changeName(savedMember.getId(), request);

        // then
        MemberResponse foundMember = oAuthService.findMember(savedMember.getId());
        assertThat(foundMember.getName()).isEqualTo("변경하려는 이름");
    }
}
