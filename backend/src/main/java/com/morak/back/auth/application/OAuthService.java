package com.morak.back.auth.application;

import com.morak.back.auth.application.dto.OAuthAccessTokenResponse;
import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.AuthorizationException;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.auth.ui.dto.SigninResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional
public class OAuthService {

    private final MemberRepository memberRepository;
    private final OAuthClient oAuthClient;
    private final TokenProvider tokenProvider;

    public OAuthService(MemberRepository memberRepository,
                        OAuthClient oAuthClient,
                        TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.oAuthClient = oAuthClient;
        this.tokenProvider = tokenProvider;
    }

    public SigninResponse signin(SigninRequest request) {
        OAuthMemberInfoResponse memberResponse = getOAuthMemberInfo(request.getCode());

        Member member = findOrSaveMember(memberResponse);

        String token = tokenProvider.createToken(String.valueOf(member.getId()));

        return new SigninResponse(token);
    }

    private OAuthMemberInfoResponse getOAuthMemberInfo(String code) {
        try {
            OAuthAccessTokenResponse tokenResponse = oAuthClient.getAccessToken(code);
            return oAuthClient.getMemberInfo(tokenResponse.getAccessToken());
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException("깃허브에서 사용자 정보를 받아오는 데 실패했습니다.");
        }
    }

    private Member findOrSaveMember(OAuthMemberInfoResponse memberResponse) {
        return memberRepository.findByOauthId(memberResponse.getOauthId())
                .orElseGet(() -> memberRepository.save(memberResponse.toMember()));
    }
}

