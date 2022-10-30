package com.morak.back.poll.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_초대코드_생성을_요청한다;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.application.dto.MemberResultResponse;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class PollAcceptanceTest extends AcceptanceTest {

    private String token;
    private String teamLocation;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
        token = tokenProvider.createToken(String.valueOf(1L));
        teamLocation = post("/api/groups", new TeamCreateRequest("팀"), toHeader(token)).header("Location");
    }

    @Test
    void 투표를_생성한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));

        // when
        ExtractableResponse<Response> response = 투표_생성을_요청한다(teamLocation, request, token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 투표를_생성할_때_잘못된_토큰이_들어오면_UNAUTHORIZE을_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));

        // when
        ExtractableResponse<Response> response = 투표_생성을_요청한다(teamLocation, request, "invalidToken");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @ParameterizedTest
    @MethodSource("getPollCreateRequest")
    void 투표를_생성할_때_request에_올바른값이_들어오지않으면_BAD_REQUEST를_반환한다(PollCreateRequest pollCreateRequest) {
        // when
        ExtractableResponse<Response> response = 투표_생성을_요청한다(teamLocation, pollCreateRequest, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    private static List<Arguments> getPollCreateRequest() {
        return List.of(
                Arguments.of(
                        new PollCreateRequest(" ", 1, false, LocalDateTime.now().plusDays(1), List.of("항목1", "항목2"))),
                Arguments.of(new PollCreateRequest("투표_제목", null, false, LocalDateTime.now().plusDays(1),
                        List.of("항목1", "항목2"))),
                Arguments.of(new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().minusDays(1),
                        List.of("항목1", "항목2"))),
                Arguments.of(new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1), null))
        );
    }

    @Test
    void 투표를_생성할_때_body가_없으면_BAD_REQUEST를_반환한다() {
        // when
        ExtractableResponse<Response> response = post(teamLocation + "/polls", "", toHeader(token));

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    @Test
    void 토큰이_없는_경우_투표를_생성_시_UNAUTHORIZED을_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));

        // when
        ExtractableResponse<Response> response = post(teamLocation + "/polls", request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.EMPTY_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 없는_멤버가_투표를_생성_시_NOT_FOUND를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        // when
        ExtractableResponse<Response> response =
                post(teamLocation + "/polls", request, toHeader(tokenProvider.createToken(0L + "")));

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 투표를_진행한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId = pollItemResponses.get(0).getId();
        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(pollItemId, "눈물이 나기 때문이에요"));

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 재투표를_진행한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId1 = pollItemResponses.get(0).getId();
        투표_진행을_요청한다(pollLocation, List.of(new PollResultRequest(pollItemId1, "눈물이_나기_때문이에요")), token);

        Long pollItemId2 = pollItemResponses.get(1).getId();

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation,
                List.of(new PollResultRequest(pollItemId2, "눈물 안나")), token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @MethodSource("getPollResultRequest")
    void 투표를_진행할_때_잘못된_pollResultRequst가_요청되면_BAD_REQUEST를_반환한다(PollResultRequest pollResultRequest) {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        List<PollResultRequest> pollResultRequests = List.of(pollResultRequest);

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    private static List<Arguments> getPollResultRequest() {
        return List.of(
                Arguments.of(new PollResultRequest()),
                Arguments.of(new PollResultRequest(null, "집 가고 싶다."))
        );
    }

    @Test
    void 투표_진행_시_description이_null이면_BAD_REQUEST를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId = pollItemResponses.get(0).getId();
        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(pollItemId, null));

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    @Test
    void 없는_투표를_진행하려고하면_NOT_FOUND를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId = pollItemResponses.get(0).getId();
        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(pollItemId, "집 가고 싶다 ~"));

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation + "invalidCode", pollResultRequests, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 없는_투표항목에_투표하면_NOT_FOUND를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(0L, "집 보내줘"));

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        String pollCode = extractPollCodeFromLocation(pollLocation);

        // when
        ExtractableResponse<Response> response = 투표_목록_조회를_요청한다(teamLocation, token);
        List<PollResponse> pollResponses = toObjectList(response, PollResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponses)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(
                                List.of(new PollResponse(null, request.getTitle(), request.getAllowedPollCount(),
                                        request.getAnonymous(),
                                        PollStatus.OPEN.name(), null, request.getClosedAt().withNano(0), pollCode, true,
                                        0))
                        )
        );
    }

    @Test
    void 투표_목록을_조회할_때_잘못된_토큰이_들어오면_UNAUTHORIZE를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_목록_조회를_요청한다(teamLocation, "invalidToken");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 투표_단건을_조회한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        String pollCode = extractPollCodeFromLocation(pollLocation);

        // when
        ExtractableResponse<Response> response = 투표_단건_조회를_요청한다(pollLocation, token);
        PollResponse pollResponse = response.as(PollResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponse)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(new PollResponse(null, request.getTitle(), request.getAllowedPollCount(),
                                request.getAnonymous(),
                                PollStatus.OPEN.name(), null, request.getClosedAt().withNano(0), pollCode, true, 0))
        );
    }

    @Test
    void 투표_진행_후_투표_단건을_조회한다() {
        // given
        String 항목1 = "항목1";
        String 항목2 = "항목2";
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of(항목1, 항목2));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");
        String pollCode = extractPollCodeFromLocation(pollLocation);

        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId = pollItemResponses.get(0).getId();
        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(pollItemId, "눈물이 나기 때문이에요"));
        투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        String otherMemberToken = tokenProvider.createToken(String.valueOf(2L));
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        그룹_참가를_요청한다(teamInvitationLocation, otherMemberToken);
        투표_진행을_요청한다(pollLocation, pollResultRequests, otherMemberToken);

        // when
        ExtractableResponse<Response> response = 투표_단건_조회를_요청한다(pollLocation, token);
        PollResponse pollResponse = response.as(PollResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponse)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(new PollResponse(null, request.getTitle(), request.getAllowedPollCount(),
                                request.getAnonymous(),
                                PollStatus.OPEN.name(), null, request.getClosedAt().withNano(0), pollCode, true, 2))
        );
    }

    @Test
    void 없는_투표를_단건_조회_시_NOT_FOUND를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_단건_조회를_요청한다(pollLocation + "invalidPoll", token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 없는_멤버가_투표를_단건_조회_시_UNAUTHORIZED를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_단건_조회를_요청한다(pollLocation, "invalidToken");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        String 항목1 = "항목1";
        String 항목2 = "항목2";
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of(항목1, 항목2));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(pollLocation, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(SimpleRestAssured.toObjectList(response, PollItemResponse.class))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(new PollItemResponse(null, 항목1, false, ""),
                                new PollItemResponse(null, 항목2, false, "")))
        );
    }

    @Test
    void 잘못된_토큰으로_투표_선택_항목을_조회하면_UNAUTHORIZED를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(pollLocation, "invalidToken");

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 없는_투표의_투표_선택_항목을_조회하면_NOT_FOUND를_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(pollLocation + "invalidPoll", token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR.getNumber())
        );
    }


    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        String subject1 = "항목1";
        String subject2 = "항목2";
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of(subject1, subject2));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId1 = pollItemResponses.get(0).getId();
        Long pollItemId2 = pollItemResponses.get(1).getId();
        PollResultRequest pollResultRequest = new PollResultRequest(pollItemId1, "주말 좋다");
        List<PollResultRequest> pollResultRequests = List.of(pollResultRequest);

        투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(pollLocation, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(toObjectList(response, PollItemResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(
                                List.of(new PollItemResponse(pollItemId1, subject1, true,
                                                pollResultRequest.getDescription()),
                                        new PollItemResponse(pollItemId2, subject2, false, ""))
                        )
        );
    }

    @Test
    void 무기명_투표_결과를_조회한다() {
        // given
        String subject1 = "subject1";
        String subject2 = "subject2";
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, true,
                LocalDateTime.now().plusDays(1), List.of(subject1, subject2));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId1 = pollItemResponses.get(0).getId();
        Long pollItemId2 = pollItemResponses.get(1).getId();
        String description = "주말 좋다";
        PollResultRequest pollResultRequest = new PollResultRequest(pollItemId1, description);
        List<PollResultRequest> pollResultRequests = List.of(pollResultRequest);

        투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // when
        ExtractableResponse<Response> response = 투표_결과_조회를_요청한다(pollLocation, token);
        List<PollItemResultResponse> pollItemResultResponses = toObjectList(response, PollItemResultResponse.class);

        Member anonymous = Member.getAnonymousMember();
        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResultResponses)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(new PollItemResultResponse(pollItemId1, 1,
                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
                                                anonymous.getProfileUrl(), description)), subject1),
                                new PollItemResultResponse(pollItemId2, 0, List.of(), subject2)))
        );
    }

    // TODO: 2022/08/13 data.sql 사용중
    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        String subject1 = "subject1";
        String subject2 = "subject2";
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of(subject1, subject2));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, token),
                PollItemResponse.class);

        Long pollItemId1 = pollItemResponses.get(0).getId();
        Long pollItemId2 = pollItemResponses.get(1).getId();
        String description = "주말 좋다";
        PollResultRequest pollResultRequest = new PollResultRequest(pollItemId1, description);
        List<PollResultRequest> pollResultRequests = List.of(pollResultRequest);

        투표_진행을_요청한다(pollLocation, pollResultRequests, token);

        // when
        ExtractableResponse<Response> response = 투표_결과_조회를_요청한다(pollLocation, token);
        List<PollItemResultResponse> pollItemResultResponses = toObjectList(response, PollItemResultResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResultResponses)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(new PollItemResultResponse(pollItemId1, 1,
                                        List.of(new MemberResultResponse(1L, "eden", "http://eden-profile.com", description)),
                                        subject1), new PollItemResultResponse(pollItemId2, 0, List.of(), subject2)
                                )
                        )
        );
    }

    @Test
    void 투표를_삭제한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, true, LocalDateTime.now().plusDays(1),
                List.of("subject1", "subject2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_삭제를_요청한다(pollLocation, token);
        ExtractableResponse<Response> findPollResponse = 투표_단건_조회를_요청한다(pollLocation, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(findPollResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(findPollResponse))
                        .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void 투표를_마감한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, true, LocalDateTime.now().plusDays(1),
                List.of("subject1", "subject2"));
        String pollLocation = 투표_생성을_요청한다(teamLocation, request, token).header("Location");

        // when
        ExtractableResponse<Response> response = 투표_마감을_요청한다(pollLocation, token);
        ExtractableResponse<Response> findPollResponse = 투표_단건_조회를_요청한다(pollLocation, token);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(SimpleRestAssured.toObject(findPollResponse, PollResponse.class))
                        .extracting("status")
                        .isEqualTo(PollStatus.CLOSED.name())
        );
    }

    private ExtractableResponse<Response> 투표_생성을_요청한다(String teamLocation, PollCreateRequest request,
                                                      String accessToken) {
        return post(teamLocation + "/polls", request, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_선택항목_조회를_요청한다(String pollLocation, String token) {
        return get(pollLocation + "/items", toHeader(token));
    }

    private ExtractableResponse<Response> 투표_진행을_요청한다(String location, List<PollResultRequest> requests, String token) {
        return put(location, requests, toHeader(token));
    }

    private ExtractableResponse<Response> 투표_목록_조회를_요청한다(String teamLocation, String token) {
        return get(teamLocation + "/polls", toHeader(token));
    }

    private ExtractableResponse<Response> 투표_마감을_요청한다(String location, String accessToken) {
        return patch(location + "/close", toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_단건_조회를_요청한다(String location, String token) {
        return get(location, toHeader(token));
    }

    private ExtractableResponse<Response> 투표_결과_조회를_요청한다(String location, String token) {
        return get(location + "/result", toHeader(token));
    }

    private ExtractableResponse<Response> 투표_삭제를_요청한다(String location, String token) {
        return delete(location, toHeader(token));
    }

    private String extractPollCodeFromLocation(String pollLocation) {
        return pollLocation.split("/")[5];
    }
}
