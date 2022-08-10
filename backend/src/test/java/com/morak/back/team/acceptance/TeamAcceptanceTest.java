package com.morak.back.team.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.AcceptanceTest;
import com.morak.back.AuthSupporter;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.ui.dto.ExceptionResponse;
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
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups")
        );
    }

    @Test
    void null인_값으로_그룹_생성_요청_시_400을_반환한() {
        // given
        TeamCreateRequest request = new TeamCreateRequest(null);

        // when
        ExtractableResponse<Response> response = 그룹_생성을_요청한다(request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.as(ExceptionResponse.class))
                        .extracting("codeNumber")
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    @Test
    void 그룹_초대_코드를_생성한다() {
        // given
        String location = 기본_그룹_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_초대코드_생성을_요청한다(location);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).startsWith("/api/groups/in")
        );
    }

    @Test
    void 그룹_초대_코드가_존재하지_않으면_NOT_FOUND를_반환한다() {
        // given
        String invalidTeamInvitationCode = "invalidTeamInvitationCode";

        // when
        ExtractableResponse<Response> response = get("api/groups/in/" + invalidTeamInvitationCode, toHeader(token));

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(response.as(ExceptionResponse.class))
                        .extracting("codeNumber")
                        .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 그룹에_참가한_멤버의_그룹_참가_여부를_확인한다() {
        // given
        String teamLocation = 기본_그룹_생성을_요청한다().header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(teamInvitationLocation, token);
        InvitationJoinedResponse isJoinedResponse = response.as(InvitationJoinedResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(isJoinedResponse.getGroupCode()).hasSize(8),
                () -> assertThat(isJoinedResponse.getName()).isEqualTo("albur"),
                () -> assertThat(isJoinedResponse.getIsJoined()).isTrue()
        );
    }

    @Test
    void 그룹에_참가하지_않은_멤버의_그룹_참가_여부를_확인한다() {
        // given
        String teamLocation = 기본_그룹_생성을_요청한다().header("Location");

        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation).header("Location");

        // when
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        ExtractableResponse<Response> response = 그룹_참가_여부_조회를_요청한다(teamInvitationLocation, otherToken);

        InvitationJoinedResponse isJoinedResponse = response.as(InvitationJoinedResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(isJoinedResponse.getGroupCode()).hasSize(8),
                () -> assertThat(isJoinedResponse.getName()).isEqualTo("albur"),
                () -> assertThat(isJoinedResponse.getIsJoined()).isFalse()
        );
    }

    @Test
    void 그룹에_참가한다() {
        // given
        String teamLocation = 기본_그룹_생성을_요청한다().header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation).header("Location");

        // when
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        ExtractableResponse<Response> response = 그룹_참가를_요청한다(teamInvitationLocation, otherToken);
        String location = response.header("Location");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(location).startsWith("/api/groups/")
        );
    }

    @Test
    void 그룹_목록을_조회한다() {
        // given
        그룹_생성을_요청한다(new TeamCreateRequest("group-A")).header("Location");
        그룹_생성을_요청한다(new TeamCreateRequest("group-B")).header("Location");

        // when
        ExtractableResponse<Response> response = 그룹_목록_조회를_요청한다(token);
        List<TeamResponse> teamResponses = toObjectList(response, TeamResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(teamResponses).hasSize(3),
                () -> assertThat(teamResponses.get(0).getName()).isEqualTo("morak"),
                () -> assertThat(teamResponses.get(1).getName()).isEqualTo("group-A"),
                () -> assertThat(teamResponses.get(2).getName()).isEqualTo("group-B")
        );
    }

    @Test
    void 그룹의_멤버들을_조회한다() {
        // given
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        String teamLocation = 기본_그룹_생성을_요청한다().header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation).header("Location");
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
    void 기본_그룹을_조회한다() {
        String otherToken = tokenProvider.createToken(String.valueOf(4L));

        String targetName = "AAA";
        사용자로_그룹_생성을_요청한다(new TeamCreateRequest(targetName), otherToken);
        사용자로_그룹_생성을_요청한다(new TeamCreateRequest("BBB"), otherToken);
        사용자로_그룹_생성을_요청한다(new TeamCreateRequest("CCC"), otherToken);

        ExtractableResponse<Response> response = get("/api/groups/default", toHeader(otherToken));

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TeamResponse.class).getName()).isEqualTo(targetName)
        );
    }


    @Test
    void 그룹을_탈퇴한다() {
        // given
        String teamLocation = 기본_그룹_생성을_요청한다().header("Location");
        String teamCode = teamLocation.split("/")[3];

        // when
        ExtractableResponse<Response> response = 그룹_탈퇴를_요청한다(teamCode);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 그룹_탈퇴를_요청한다(String teamCode) {
        return delete("/api/groups/out/" + teamCode, toHeader(token));
    }

    private ExtractableResponse<Response> 사용자로_그룹_생성을_요청한다(TeamCreateRequest request, String token) {
        return post("/api/groups", request, toHeader(token));
    }

    private ExtractableResponse<Response> 그룹_생성을_요청한다(TeamCreateRequest request) {
        return 사용자로_그룹_생성을_요청한다(request, token);
    }

    private ExtractableResponse<Response> 기본_그룹_생성을_요청한다() {
        TeamCreateRequest request = new TeamCreateRequest("albur");
        return 그룹_생성을_요청한다(request);
    }

    private ExtractableResponse<Response> 그룹_초대코드_생성을_요청한다(String location) {
        return post(location + "/invitation", "", toHeader(token));
    }

    private ExtractableResponse<Response> 그룹_참가_여부_조회를_요청한다(String teamInvitationLocation, String token) {
        return get(teamInvitationLocation, toHeader(token));
    }

    private ExtractableResponse<Response> 그룹_참가를_요청한다(String teamInvitationLocation, String otherToken) {
        return post(teamInvitationLocation, "", AuthSupporter.toHeader(otherToken));
    }

    private ExtractableResponse<Response> 그룹_목록_조회를_요청한다(String token) {
        return get("/api/groups", AuthSupporter.toHeader(token));
    }

    private ExtractableResponse<Response> 그룹_멤버_목록_조회를_요청한다(String token, String teamLocation) {
        return get(teamLocation + "/members", AuthSupporter.toHeader(token));
    }
}
