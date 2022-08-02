package com.morak.back.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.auth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OAuthMemberInfoResponse {

    @JsonProperty("id")
    private String oauthId;

    @JsonProperty("login")
    private String name;

    @JsonProperty("avatar_url")
    private String profileUrl;

    public Member toMember() {
        return Member.builder()
                .oauthId(oauthId)
                .name(name)
                .profileUrl(profileUrl)
                .build();
    }
}
