package com.morak.back.auth.application;

import com.morak.back.auth.application.dto.OAuthAccessTokenResponse;
import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;

public interface OAuthClient {

    OAuthAccessTokenResponse getAccessToken(String code);

    OAuthMemberInfoResponse getMemberInfo(String accessToken);
}
