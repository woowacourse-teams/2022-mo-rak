package com.morak.back.team.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
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

    @DisplayName("그룹에 참가한다.")
    @Test
    void joinTeam() {
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
        String location = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + otherToken)
                .post(teamInvitationLocation)
                .then().log().all().extract()
                .header("Location");

        assertThat(location).startsWith("/groups/");
    }

    @DisplayName("그룹 목록을 조회한다.")
    @Test
    void findTeams() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        String teamALocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(new TeamCreateRequest("group-A")).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");

        String teamBLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(new TeamCreateRequest("group-B")).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");
        // when

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .get("/groups")
                .then().log().all().extract();
        List<TeamResponse> teamResponses = response.jsonPath().getList(".", TeamResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(teamResponses).hasSize(3),
                () -> assertThat(teamResponses.get(0).getName()).isEqualTo("morak"),
                () -> assertThat(teamResponses.get(1).getName()).isEqualTo("group-A"),
                () -> assertThat(teamResponses.get(2).getName()).isEqualTo("group-B")
        );
    }

    @DisplayName("그룹의 멤버들을 조회한다.")
    @Test
    void findMembers() {
        // given
        String member1Token = tokenProvider.createToken(String.valueOf(1L));
        String member2Token = tokenProvider.createToken(String.valueOf(2L));

        String teamLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + member1Token)
                .body(new TeamCreateRequest("group-A")).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");

        String invitationLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + member1Token)
                .post(teamLocation + "/invitation")
                .then().log().all()
                .extract().header("Location");

        RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + member2Token)
                .post(invitationLocation)
                .then().log().all();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + member1Token)
                .get(teamLocation + "/members")
                .then().log().all().extract();

        List<MemberResponse> teamResponses = response.jsonPath().getList(".", MemberResponse.class);

        // then
        assertThat(teamResponses).extracting("name", "profileUrl")
                .containsExactly(
                        tuple("eden", "eden-profile.com"),
                        tuple("ellie", "ellie-profile.com")
                );
    }

    @DisplayName("그룹을 탈퇴한다.")
    @Test
    void exitTeam() {
        // given
        String memberToken = tokenProvider.createToken(String.valueOf(1L));
        String teamLocation = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + memberToken)
                .body(new TeamCreateRequest("group-A")).contentType(MediaType.APPLICATION_JSON_VALUE).post("/groups")
                .then().log().all().extract().header("Location");
        String teamCode = teamLocation.split("/")[2];
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + memberToken)
                .delete("/groups/out/" + teamCode)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
