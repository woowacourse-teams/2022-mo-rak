package com.morak.back.team.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
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

    @DisplayName("그룹 초대 코드를 생성한다.")
    @Test
    void createInvitationCode() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));

        TeamCreateRequest request = new TeamCreateRequest("albur");
        String location = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .post(location + "/invitation")
                .then().log().all()
                .extract();

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/groups/in")
        );
    }
    
    @DisplayName("그룹에 참가한 멤버의 그룹 참가 여부를 확인한다.")
    @Test
    void isJoinedWithAlreadyJoined() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");
        String teamInvitationLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .post(teamLocation + "/invitation")
                .then().log().all()
                .extract().header("Location");
        // when
        ExtractableResponse<Response> isJoinedResponse = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .get(teamInvitationLocation)
                .then().log().all()
                .extract();
        InvitationJoinedResponse response = isJoinedResponse.jsonPath().getObject(".", InvitationJoinedResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(response.getGroupCode()).hasSize(8),
                () -> assertThat(response.getName()).isEqualTo("albur"),
                () -> assertThat(response.getIsJoined()).isTrue()
        );
    }

    @DisplayName("그룹에 참가하지 않은 멤버의 그룹 참가 여부를 확인한다.")
    @Test
    void isJoinedWithNotJoined() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");
        String teamInvitationLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .post(teamLocation + "/invitation")
                .then().log().all()
                .extract().header("Location");
        // when
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        ExtractableResponse<Response> isJoinedResponse = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + otherToken)
                .get(teamInvitationLocation)
                .then().log().all()
                .extract();
        InvitationJoinedResponse response = isJoinedResponse.jsonPath().getObject(".", InvitationJoinedResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(response.getGroupCode()).hasSize(8),
                () -> assertThat(response.getName()).isEqualTo("albur"),
                () -> assertThat(response.getIsJoined()).isFalse()
        );
    }
}
