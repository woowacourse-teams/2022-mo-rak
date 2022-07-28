package com.morak.back.poll.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class PollAcceptanceTest extends AcceptanceTest {

    private static final String INVALID_ACCESS_TOKEN = "invalid.access.token";

    private String accessToken;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
        accessToken = tokenProvider.createToken(String.valueOf(1L));
    }

    @Test
    void 투표를_생성한다() {
        // given & when
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        ExtractableResponse<Response> response = 투표_생성을_요청한다(request, accessToken);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        String path = "/api/groups/MoraK123/polls";

        // when
        ExtractableResponse<Response> response = 투표_목록_조회를_요청한다(path);
        List<PollResponse> pollResponses = toObjectList(response, PollResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponses).hasSize(1)
        );
    }

    @Test
    void 투표를_진행한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");
        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이 나기 때문이에요"));

        // when
        ExtractableResponse<Response> response = 투표_진행을_요청한다(location, pollItemRequests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 재투표를_진행한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이_나기_때문이에요"));
        투표_진행을_요청한다(location, pollItemRequests);

        // when
        List<PollItemRequest> rePollItemRequests = List.of(new PollItemRequest(5L, "다시_일어설거에요!"));
        ExtractableResponse<Response> rePollResponse = 투표_진행을_요청한다(location, rePollItemRequests);

        // then
        assertThat(rePollResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 투표_단건을_조회한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 투표_단건_조회를_요청한다(location);
        PollResponse pollResponse = response.as(PollResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponse.getTitle()).isEqualTo("투표_제목"),
                () -> assertThat(pollResponse.getIsHost()).isTrue()
        );
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(location);
        List<PollItemResponse> pollItemResponses = toObjectList(response, PollItemResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getId()).isNotNull(),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1"),
                () -> assertThat(pollItemResponses.get(0).getSelected()).isFalse(),
                () -> assertThat(pollItemResponses.get(0).getDescription()).isBlank()
        );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");
        투표_진행을_요청한다(location, List.of(new PollItemRequest(4L, "월요일이기때문!!")));

        // when
        ExtractableResponse<Response> response = 투표_선택항목_조회를_요청한다(location);
        List<PollItemResponse> pollItemResponses = toObjectList(response, PollItemResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getId()).isNotNull(),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1"),
                () -> assertThat(pollItemResponses.get(0).getSelected()).isTrue(),
                () -> assertThat(pollItemResponses.get(0).getDescription()).isEqualTo("월요일이기때문!!"),
                () -> assertThat(pollItemResponses.get(1).getSubject()).isEqualTo("항목2"),
                () -> assertThat(pollItemResponses.get(1).getSelected()).isFalse(),
                () -> assertThat(pollItemResponses.get(1).getDescription()).isBlank()
        );
    }

    @Test
    void 무기명_투표_결과를_조회한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표_제목", 2, true, LocalDateTime.now().plusDays(1L),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표_생성을_요청한다(request, accessToken).header("Location");

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이_나기_때문이에요"));
        투표_진행을_요청한다(location, pollItemRequests);

        // when
        ExtractableResponse<Response> response = 투표_결과_조회를_요청한다(location);
        List<PollItemResultResponse> resultResponses = toObjectList(response, PollItemResultResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(3),
                () -> assertThat(resultResponses.get(0).getCount()).isEqualTo(1),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(1).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(2).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getName()).isEqualTo(Member.getAnonymous().getName()),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo("눈물이_나기_때문이에요")
        );
    }

    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이_나기_때문이에요"));
        투표_진행을_요청한다(location, pollItemRequests);

        // when
        ExtractableResponse<Response> response = 투표_결과_조회를_요청한다(location);
        List<PollItemResultResponse> resultResponses = toObjectList(response, PollItemResultResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(2),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getName()).isEqualTo("eden"),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo("눈물이_나기_때문이에요"),
                () -> assertThat(resultResponses.get(1).getMembers()).hasSize(0)
        );
    }

    @Test
    void null인_값으로_투표요청시_400을_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest(null, 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));

        // when
        ExtractableResponse<Response> response = 투표_생성을_요청한다(request, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 도메인_정책에_위반한_값으로_투표요청시_400을_반환한다() {
        // given
        PollCreateRequest request = new PollCreateRequest("하이", 0, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));

        // when
        ExtractableResponse<Response> response = 투표_생성을_요청한다(request, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 투표를_삭제한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 투표_삭제를_요청한다(location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 투표를_마감한다() {
        // given
        String location = 기본_투표_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 투표_마감을_요청한다(location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 호스트가_아닌_사용자가_투표마감시_BAD_REQUEST를_응답한다() {
        // given
        String otherToken = tokenProvider.createToken(2L + "");
        String location = 기본_투표_생성을_요청한다().header("Location");

        // when
        ExtractableResponse<Response> response = 투표_마감을_요청한다(location, otherToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 투표_목록_조회를_요청한다(String path) {
        return get(path, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_진행을_요청한다(String location, List<PollItemRequest> requests) {
        return put(location, requests, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_마감을_요청한다(String location) {
        return 투표_마감을_요청한다(location, accessToken);
    }

    private ExtractableResponse<Response> 투표_마감을_요청한다(String location, String accessToken) {
        return patch(location + "/close", toHeader(accessToken));
    }

    private ExtractableResponse<Response> 기본_투표_생성을_요청한다() {
        PollCreateRequest request = new PollCreateRequest("투표_제목", 1, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        return 투표_생성을_요청한다(request, accessToken);
    }

    private ExtractableResponse<Response> 투표_생성을_요청한다(PollCreateRequest request, String accessToken) {
        return post("/api/groups/MoraK123/polls", request, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_단건_조회를_요청한다(String location) {
        return get(location, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_결과_조회를_요청한다(String location) {
        return get(location + "/result", toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_선택항목_조회를_요청한다(String location) {
        return get(location + "/items", toHeader(accessToken));
    }

    private ExtractableResponse<Response> 투표_삭제를_요청한다(String location) {
        return delete(location, toHeader(accessToken));
    }
}
