package com.morak.back.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
