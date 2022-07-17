package com.morak.back.team.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TeamAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("그룹을 생성한다.")
    @Test
    void createTeam() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String token = tokenProvider.createToken(String.valueOf(1L));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/groups");
    }
}
