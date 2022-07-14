package com.morak.back.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.auth.domain.Member2;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OAuthMemberInfoResponse {

    @JsonProperty("id")
    private Long oauthId;

    @JsonProperty("login")
    private String name;

    @JsonProperty("avatar_url")
    private String profile_url;

    public Member2 toMember2() {
        return new Member2(null, oauthId, name, profile_url);
    }
}
