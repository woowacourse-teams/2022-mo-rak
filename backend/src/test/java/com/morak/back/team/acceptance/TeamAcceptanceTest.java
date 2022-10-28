package com.morak.back.team.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.AcceptanceTest;
import com.morak.back.AuthSupporter;
import com.morak.back.SimpleRestAssured;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class TeamAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TokenProvider tokenProvider;

    private String token;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        token = tokenProvider.createToken(String.valueOf(1L));
    }

    @Test
    void 그룹을_생성한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");

        // when
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups")
        );
    }

    @Test
    void 그룹을_생성_시_토큰이_없으면_UNAUTHORIZED을_반환한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");

        // when
        ExtractableResponse<Response> response = post("/api/groups", request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.EMPTY_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 그룹을_생성_시_잘못된_토큰이_들어오면_UNAUTHORIZED을_반환한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");

        // when
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request, "invalidToken");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 없는_멤버가_그룹을_생성_시_FORBIDDEN을_반환한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String notExistMemberToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request, notExistMemberToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void null인_값으로_그룹_생성_요청_시_BAD_REQUEST를_반환한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest(null);

        // when
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    @Test
    void 그룹_초대_코드를_생성한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String location = 그룹_생성을_요청한다(request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_초대코드_생성을_요청한다(location, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups/in/")
        );
    }

    @Test
    void 그룹_초대_코드를_여러번_생성할_수_있다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String location = 그룹_생성을_요청한다(request, token).header("Location");
        그룹_초대코드_생성을_요청한다(location, token);

        // when
        ExtractableResponse<Response> response = 그룹_초대코드_생성을_요청한다(location, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups/in/")
        );
    }

    @Test
    void 그룹_초대_코드가_존재하지_않으면_NOT_FOUND를_반환한다() {
        // given
        String invalidTeamInvitationCode = "invalidTeamInvitationCode";

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다("api/groups/in/" + invalidTeamInvitationCode, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹에_참가한_멤버의_그룹_참가_여부를_확인한다() {
        // given
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest("하이");
        String teamLocation = 그룹_생성을_요청한다(teamCreateRequest, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(teamInvitationLocation, token);

        // then
        InvitationJoinedResponse invitationJoinedResponse = response.as(InvitationJoinedResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(invitationJoinedResponse)
                        .usingRecursiveComparison()
                        .isEqualTo(new InvitationJoinedResponse(extractTeamCodeFromLocation(teamLocation),
                                teamCreateRequest.getName(),
                                true))
        );
    }

    @Test
    void 그룹에_참가하지_않은_멤버의_그룹_참가_여부를_확인한다() {
        // given
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest("하이");
        String teamLocation = 그룹_생성을_요청한다(teamCreateRequest, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(2L));

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(teamInvitationLocation, otherToken);

        // then
        InvitationJoinedResponse invitationJoinedResponse = response.as(InvitationJoinedResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(invitationJoinedResponse)
                        .usingRecursiveComparison()
                        .isEqualTo(new InvitationJoinedResponse(extractTeamCodeFromLocation(teamLocation),
                                teamCreateRequest.getName(),
                                false))
        );
    }

    @Test
    void 없는_멤버가_그룹_참가_여부를_요청하면_NOT_FOUND를_반환한다() {
        // given
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest("하이");
        String teamLocation = 그룹_생성을_요청한다(teamCreateRequest, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(teamInvitationLocation, otherToken);
        String s = SimpleRestAssured.extractCodeNumber(response);
        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 없는_그룹코드로_그룹_참가_여부를_요청하면_NOT_FOUND를_반환한다() {
        // given
        String teamInvitationLocation = "/api/groups/in/" + "invalidTeamInvitationCode";

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(
                teamInvitationLocation,
                token
        );

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹에_참가한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(2L));

        // when
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(teamInvitationLocation, otherToken);
        String location = response.header("Location");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(location).startsWith("/api/groups/")
        );
    }

    @Test
    void 없는_멤버가_그룹에_참가하면_NOT_FOUND가_반환된다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(teamInvitationLocation, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 없는_그룹에_참가하면_NOT_FOUND가_반환된다() {
        // given
        String teamInvitationLocation = "/api/groups/in/" + "invalidTeamInvitationCode";

        // when
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(teamInvitationLocation, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹에_참가한_멤버가_또_같은_그룹에_참가하면_400을_반환한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(2L));

        // when
        그룹_참가를_요청한다(teamInvitationLocation, otherToken);
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(teamInvitationLocation, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_ALREADY_JOINED_ERROR.getNumber())
        );
    }

    @Test
    void 이미_그룹에_참가한_멤버가_다른_그룹에_참가할_수_있다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String team1Location = 그룹_생성을_요청한다(request, token).header("Location");
        String team2Location = 그룹_생성을_요청한다(request, token).header("Location");
        String team1InvitationLocation = 그룹_초대코드_생성을_요청한다(team1Location, token).header("Location");
        String team2InvitationLocation = 그룹_초대코드_생성을_요청한다(team2Location, token).header("Location");

        String otherToken = tokenProvider.createToken(String.valueOf(2L));

        // when
        그룹_참가를_요청한다(team1InvitationLocation, otherToken);
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(team2InvitationLocation, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("location")).startsWith("/api/groups/")
        );
    }

    @Test
    void 그룹_목록을_조회한다() {
        // given
        TeamCreateRequest requestA = new TeamCreateRequest("team-A");
        TeamCreateRequest requestB = new TeamCreateRequest("team-B");
        String teamALocation1 = 그룹_생성을_요청한다(requestA, token).header("Location");
        그룹_생성을_요청한다(requestB, token).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_목록_조회를_요청한다(token);
        List<TeamResponse> teamResponses = toObjectList(response, TeamResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(teamResponses)
                        .extracting("name")
                        .containsExactly("morak", requestA.getName(), requestB.getName())
        );
    }

    @Test
    void 없는_멤버가_그룹_목록을_조회하면_NOT_FOUND를_반환한다() {
        // given
        String invalidToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_목록_조회를_요청한다(invalidToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹의_멤버들을_조회한다() {
        // given
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        그룹_참가를_요청한다(teamInvitationLocation, otherToken);

        // when
        ExtractableResponse<Response> response = 그룹_멤버_목록_조회를_요청한다(token, teamLocation);

        List<MemberResponse> memberResponses = toObjectList(response, MemberResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberResponses).extracting("name", "profileUrl")
                        .containsExactly(
                                tuple("eden", "http://eden-profile.com"),
                                tuple("ellie", "http://ellie-profile.com")
                        )
        );
    }

    @Test
    void 없는_그룹의_멤버들을_조회하면_NOT_FOUND가_반환된다() {
        // given
        String invalidTeamLocation = "/api/groups/invalidGroupCode";

        // when
        ExtractableResponse<Response> response = 그룹_멤버_목록_조회를_요청한다(token, invalidTeamLocation);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹에_속하지않은_멤버가_그룹의_멤버들을_조회하면_FORBIDDEN이_반환된다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(2L));

        // when
        ExtractableResponse<Response> response = 그룹_멤버_목록_조회를_요청한다(otherToken, teamLocation);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR.getNumber())
        );
    }

    @Test
    void 없는_멤버가_그룹의_멤버들을_조회하면_NOT_FOUND가_반환된다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String invalidToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_멤버_목록_조회를_요청한다(invalidToken, teamLocation);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 기본_그룹을_조회한다() {
        String otherToken = tokenProvider.createToken(String.valueOf(5L));

        String targetName = "AAA";
        String targetTeamLocation = 그룹_생성을_요청한다(new TeamCreateRequest(targetName), otherToken).header("Location");
        그룹_생성을_요청한다(new TeamCreateRequest("BBB"), otherToken);
        그룹_생성을_요청한다(new TeamCreateRequest("CCC"), otherToken);

        ExtractableResponse<Response> response = 기본_그룹_조회를_요청한다(otherToken);
        TeamResponse teamResponse = response.as(TeamResponse.class);

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(teamResponse)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(new TeamResponse(null, extractTeamCodeFromLocation(targetTeamLocation), targetName))
        );
    }

    @Test
    void 없는_멤버가_기본_그룹을_조회하면_NOT_FOUND가_반환된다() {
        String otherToken = tokenProvider.createToken(String.valueOf(0L));

        ExtractableResponse<Response> response = 기본_그룹_조회를_요청한다(otherToken);

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹이_없는_멤버가_기본_그룹을_조회하면_NOT_FOUND가_반환된다() {
        String otherToken = tokenProvider.createToken(String.valueOf(5L));

        ExtractableResponse<Response> response = 기본_그룹_조회를_요청한다(otherToken);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹을_탈퇴한다() {
        // given
        String otherToken = tokenProvider.createToken(String.valueOf(5L));
        String teamLocation = 그룹_생성을_요청한다(new TeamCreateRequest("하이"), otherToken).header("Location");
        String teamCode = extractTeamCodeFromLocation(teamLocation);

        // when
        ExtractableResponse<Response> teamExitResponse = 그룹_탈퇴를_요청한다(teamCode, otherToken);
        List<TeamResponse> teamResponses = toObjectList(그룹_목록_조회를_요청한다(otherToken), TeamResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(teamExitResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(teamResponses).hasSize(0)
        );
    }

    @Test
    void 이미_탈퇴한_그룹을_다시_탈퇴하면_FORBIDDEN을_반환한다() {
        // given
        String otherToken = tokenProvider.createToken(String.valueOf(5L));
        String teamLocation = 그룹_생성을_요청한다(new TeamCreateRequest("하이"), otherToken).header("Location");
        String teamCode = extractTeamCodeFromLocation(teamLocation);

        // when
        그룹_탈퇴를_요청한다(teamCode, otherToken);
        ExtractableResponse<Response> response = 그룹_탈퇴를_요청한다(teamCode, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR.getNumber())
        );
    }

    @Test
    void 없는_멤버가_그룹_탈퇴_요청을_보내면_NOT_FOUND가_반환된다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("albur");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamCode = extractTeamCodeFromLocation(teamLocation);
        String otherToken = tokenProvider.createToken(String.valueOf(0L));

        // when
        ExtractableResponse<Response> response = 그룹_탈퇴를_요청한다(teamCode, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 없는_그룹_탈퇴_요청을_보내면_NOT_FOUND가_반환된다() {
        // given
        String otherToken = tokenProvider.createToken(String.valueOf(5L));
        String invalidTeamCode = "invalidTeamCode";

        // when
        ExtractableResponse<Response> response = 그룹_탈퇴를_요청한다(invalidTeamCode, otherToken);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR.getNumber())
        );
    }

    public static ExtractableResponse<Response> 그룹_생성을_요청한다(TeamCreateRequest request, String token) {
        return post("/api/groups", request, toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_초대코드_생성을_요청한다(String location, String token) {
        return post(location + "/invitation", "", toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_참가_여부_조회를_요청한다(String teamInvitationLocation, String token) {
        return get(teamInvitationLocation, toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_참가를_요청한다(String teamInvitationLocation, String otherToken) {
        return post(teamInvitationLocation, "", AuthSupporter.toHeader(otherToken));
    }

    public static ExtractableResponse<Response> 그룹_목록_조회를_요청한다(String token) {
        return get("/api/groups", AuthSupporter.toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_멤버_목록_조회를_요청한다(String token, String teamLocation) {
        return get(teamLocation + "/members", AuthSupporter.toHeader(token));
    }

    public static ExtractableResponse<Response> 기본_그룹_조회를_요청한다(String otherToken) {
        return get("/api/groups/default", toHeader(otherToken));
    }

    public static ExtractableResponse<Response> 그룹_탈퇴를_요청한다(String teamCode, String token) {
        return delete("/api/groups/out/" + teamCode, toHeader(token));
    }

    public static String extractTeamCodeFromLocation(String teamLocation) {
        return teamLocation.split("/")[3];
    }
}
