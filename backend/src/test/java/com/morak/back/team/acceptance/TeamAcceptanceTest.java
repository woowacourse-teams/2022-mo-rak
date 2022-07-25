package com.morak.back.team.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.AcceptanceTest;
import com.morak.back.AuthSupporter;
import com.morak.back.SimpleRestAssured;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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
        ExtractableResponse<Response> response = 그룹을_생성한다(token, request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups")
        );
    }

    @DisplayName("그룹 초대 코드를 생성한다.")
    @Test
    void createInvitationCode() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));

        TeamCreateRequest request = new TeamCreateRequest("albur");
        String location = 그룹을_생성한다(token, request).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹의_초대코드를_생성한다(token, location);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups/in")
        );
    }

    @DisplayName("그룹에 참가한 멤버의 그룹 참가 여부를 확인한다.")
    @Test
    void isJoinedWithAlreadyJoined() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("albur");

        String teamLocation = 그룹을_생성한다(token, request).header("Location");

        String teamInvitationLocation = 그룹의_초대코드를_생성한다(token, teamLocation).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부를_확인한다(teamInvitationLocation, token);

        InvitationJoinedResponse isJoinedResponse = response.as(InvitationJoinedResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(isJoinedResponse.getGroupCode()).hasSize(8),
                () -> assertThat(isJoinedResponse.getName()).isEqualTo("albur"),
                () -> assertThat(isJoinedResponse.getIsJoined()).isTrue()
        );
    }

    @DisplayName("그룹에 참가하지 않은 멤버의 그룹 참가 여부를 확인한다.")
    @Test
    void isJoinedWithNotJoined() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹을_생성한다(token, request).header("Location");

        String teamInvitationLocation = 그룹의_초대코드를_생성한다(token, teamLocation).header("Location");

        // when
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        ExtractableResponse<Response> response = 그룹_참가_여부를_확인한다(teamInvitationLocation, otherToken);

        InvitationJoinedResponse isJoinedResponse = response.as(InvitationJoinedResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(isJoinedResponse.getGroupCode()).hasSize(8),
                () -> assertThat(isJoinedResponse.getName()).isEqualTo("albur"),
                () -> assertThat(isJoinedResponse.getIsJoined()).isFalse()
        );
    }

    @DisplayName("그룹에 참가한다.")
    @Test
    void joinTeam() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹을_생성한다(token, request).header("Location");

        String teamInvitationLocation = 그룹의_초대코드를_생성한다(token, teamLocation).header("Location");

        // when
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        String location = 그룹에_참가한다(teamInvitationLocation, otherToken).header("Location");

        assertThat(location).startsWith("/api/groups/");
    }

    @DisplayName("그룹 목록을 조회한다.")
    @Test
    void findTeams() {
        // given
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest requestA = new TeamCreateRequest("group-A");
        TeamCreateRequest requestB = new TeamCreateRequest("group-B");
        그룹을_생성한다(token, requestA).header("Location");
        그룹을_생성한다(token, requestB).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_목록을_조회한다(token);

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

        TeamCreateRequest request = new TeamCreateRequest("group-A");
        String teamLocation = 그룹을_생성한다(member1Token, request).header("Location");

        String teamInvitationLocation = 그룹의_초대코드를_생성한다(member1Token, teamLocation).header("Location");

        그룹에_참가한다(teamInvitationLocation, member2Token);

        // when
        ExtractableResponse<Response> response = 그룹의_멤버_목록을_조회한다(member1Token, teamLocation);

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
        String token = tokenProvider.createToken(String.valueOf(1L));
        TeamCreateRequest request = new TeamCreateRequest("group-A");

        String teamLocation = 그룹을_생성한다(token, request).header("Location");
        String teamCode = teamLocation.split("/")[3];

        // when
        ExtractableResponse<Response> response = SimpleRestAssured.delete("/api/groups/out/" + teamCode, toHeader(token));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 그룹을_생성한다(String token, TeamCreateRequest request) {
        return post("/api/groups", request, toHeader(token));
    }

    private ExtractableResponse<Response> 그룹의_초대코드를_생성한다(String token, String location) {
        return post(location + "/invitation", "", toHeader(token));
    }

    private ExtractableResponse<Response> 그룹_참가_여부를_확인한다(String teamInvitationLocation, String token) {
        return get(teamInvitationLocation, toHeader(token));
    }

    private ExtractableResponse<Response> 그룹에_참가한다(String teamInvitationLocation, String otherToken) {
        return post(teamInvitationLocation, "", AuthSupporter.toHeader(otherToken));
    }

    private ExtractableResponse<Response> 그룹_목록을_조회한다(String token) {
        return SimpleRestAssured.get("/api/groups", AuthSupporter.toHeader(token));
    }

    private ExtractableResponse<Response> 그룹의_멤버_목록을_조회한다(String token, String teamLocation) {
        return SimpleRestAssured.get(teamLocation + "/members", AuthSupporter.toHeader(token));
    }
}
