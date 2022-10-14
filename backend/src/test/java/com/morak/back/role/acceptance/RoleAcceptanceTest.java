package com.morak.back.role.acceptance;


import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.post;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class RoleAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private String teamLocation;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
        accessToken = tokenProvider.createToken(String.valueOf(1L));
        teamLocation = post("/api/groups", new TeamCreateRequest("팀"), toHeader(accessToken)).header("Location");
    }


    // -- A



    // -- B



    // -- C
    @Test
    void 역할을_매칭한다() {
        // given

        // when
        ExtractableResponse<Response> response = 역할_매칭을_요청한다();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).contains(teamLocation + "/roles/");
    }

    private ExtractableResponse<Response> 역할_매칭을_요청한다() {
        return post(teamLocation + "/roles", toHeader(accessToken));
    }

    // -- D


}
