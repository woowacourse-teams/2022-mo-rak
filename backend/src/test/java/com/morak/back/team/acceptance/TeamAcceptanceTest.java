package com.morak.back.team.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.morak.back.AcceptanceTest;
import com.morak.back.team.ui.dto.TeamCreateRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TeamAcceptanceTest extends AcceptanceTest {

    @DisplayName("그룹을 생성한다.")
    @Test
    void createTeam() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/groups");
    }
}
