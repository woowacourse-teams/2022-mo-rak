package com.morak.back.auth.application;

import com.morak.back.auth.application.dto.OAuthAccessTokenResponse;
import com.morak.back.auth.application.dto.OAuthMemberInfoResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Primary
public class FakeOAuthClient implements OAuthClient {

    public static final String ACCESS_TOKEN = "FAKE-ACCESS-TOKEN";
    public static final String OAUTH_ID = "FAKE-OAUTH-ID";
    public static final String NAME = "FAKE-NAME";
    public static final String PROFILE_URL = "http://FAKE_PROFILE_URL.com";

    public static final String FAILURE = "FAILURE";

    @Override
    public OAuthAccessTokenResponse getAccessToken(String code) {
        if (FAILURE.equals(code)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        return new OAuthAccessTokenResponse(ACCESS_TOKEN);
    }

    @Override
    public OAuthMemberInfoResponse getMemberInfo(String accessToken) {
        return new OAuthMemberInfoResponse(OAUTH_ID, NAME, PROFILE_URL);
    }
}
