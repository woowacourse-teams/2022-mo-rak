package com.morak.back.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.auth.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OAuthMemberInfoResponse {

    @JsonProperty("id")
    private String oauthId;

    @JsonProperty("login")
    private String name;

    @JsonProperty("avatar_url")
    private String profileUrl;

    public Member toMember() {
        return new Member(null, oauthId, name, profileUrl);
    }
}
