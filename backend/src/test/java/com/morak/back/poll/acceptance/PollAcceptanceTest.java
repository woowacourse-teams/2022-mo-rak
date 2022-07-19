package com.morak.back.poll.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class PollAcceptanceTest extends AcceptanceTest {

    private static final String INVALID_ACCESS_TOKEN = "invalid.access.token";

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("투표를 생성한다.")
    @Test
    void createPoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("투표 목록을 조회한다.")
    @Test
    void findPolls() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get("/polls")
                .then().log().all().extract();

        // then
        List<PollResponse> responses = response.body().jsonPath().getList(".", PollResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responses).hasSize(1)
        );
    }

    @DisplayName("투표를 진행한다.")
    @Test
    void doPoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest createRequest = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, createRequest);

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이 나기 때문이에요"));

        // when
        ExtractableResponse<Response> response = 투표를_진행한다(accessToken, location, pollItemRequests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("재투표를 진행한다.")
    @Test
    void rePoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest createRequest = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, createRequest);

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이 나기 때문이에요"));
        투표를_진행한다(accessToken, location, pollItemRequests);

        List<PollItemRequest> rePollItemRequests = List.of(new PollItemRequest(5L, "다시 일어설거에요!"));

        // when
        ExtractableResponse<Response> rePollResponse = 투표를_진행한다(accessToken, location, rePollItemRequests);

        // then
        assertThat(rePollResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("투표 단건을 조회한다.")
    @Test
    void findPoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest createRequest = new PollCreateRequest("앨버의 현재 위치", 1, false, LocalDateTime.now(),
                List.of("트랙룸", "1번 회의실"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, createRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get(location)
                .then().log().all().extract();

        // then
        PollResponse pollResponse = response.body().jsonPath().getObject(".", PollResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponse.getTitle()).isEqualTo("앨버의 현재 위치"),
                () -> assertThat(pollResponse.getIsHost()).isTrue()
        );
    }

    @DisplayName("투표 선택 항목을 조회한다.")
    @Test
    void findPollItems() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("위니의 영어 단어 사용 횟수", 1, false, LocalDateTime.now(),
                List.of("삼십만", "300000"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get(location + "/items")
                .then().log().all().extract();

        // then
        List<PollItemResponse> pollItemResponses = response.body().jsonPath().getList(".", PollItemResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getId()).isNotNull(),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("삼십만"),
                () -> assertThat(pollItemResponses.get(0).getSelected()).isFalse(),
                () -> assertThat(pollItemResponses.get(0).getDescription()).isBlank()
        );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("에덴의 속마음은?", 1, false, LocalDateTime.now(),
                List.of("에덴은 칼퇴하고 싶다.", "에덴은 11시에 퇴근하고 싶다."));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        투표를_진행한다(accessToken, location, List.of(new PollItemRequest(4L, "월요일이기때문!!")));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get(location + "/items")
                .then().log().all().extract();

        // then
        List<PollItemResponse> pollItemResponses = response.body().jsonPath().getList(".", PollItemResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getId()).isNotNull(),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("에덴은 칼퇴하고 싶다."),
                () -> assertThat(pollItemResponses.get(0).getSelected()).isTrue(),
                () -> assertThat(pollItemResponses.get(0).getDescription()).isEqualTo("월요일이기때문!!"),
                () -> assertThat(pollItemResponses.get(1).getSubject()).isEqualTo("에덴은 11시에 퇴근하고 싶다."),
                () -> assertThat(pollItemResponses.get(1).getSelected()).isFalse(),
                () -> assertThat(pollItemResponses.get(1).getDescription()).isBlank()
        );
    }

    @DisplayName("무기명 투표 결과를 조회한다.")
    @Test
    public void findPollResultsWithAnonymous() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, true, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이 나기 때문이에요"));
        투표를_진행한다(accessToken, location, pollItemRequests);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get(location + "/result")
                .then().log().all().extract();

        final List<PollItemResultResponse> resultResponses
                = response.body().jsonPath().getList(".", PollItemResultResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(3),
                () -> assertThat(resultResponses.get(0).getCount()).isEqualTo(1),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(1).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(2).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getName()).isBlank(),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo("눈물이 나기 때문이에요")
        );
    }

    @DisplayName("기명 투표 결과를 조회한다.")
    @Test
    public void findPollResults() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        List<PollItemRequest> pollItemRequests = List.of(new PollItemRequest(4L, "눈물이 나기 때문이에요"));
        투표를_진행한다(accessToken, location, pollItemRequests);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .get(location + "/result")
                .then().log().all().extract();

        final List<PollItemResultResponse> resultResponses = response.body().jsonPath()
                .getList(".", PollItemResultResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(3),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getName()).isEqualTo("eden"),
                () -> assertThat(resultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo("눈물이 나기 때문이에요"),
                () -> assertThat(resultResponses.get(1).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(2).getMembers()).hasSize(0)
        );
    }

    @DisplayName("투표를 삭제한다.")
    @Test
    public void deletePoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .delete(location)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("투표를 마감한다")
    @Test
    void closePoll() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        // when
        ExtractableResponse<Response> response = 투표를_마감한다(accessToken, location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 인증되지않은_사용자가_투표마감시_403을_응답한다() {
        // given
        String accessToken = 로그인을_해_토큰을_발급받는다(1L);

        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = 투표를_생성한_뒤_투표_URL을_받는다(accessToken, request);

        // when
        ExtractableResponse<Response> response = 투표를_마감한다(INVALID_ACCESS_TOKEN, location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private String 로그인을_해_토큰을_발급받는다(Long id) {
        return tokenProvider.createToken(String.valueOf(id));
    }

    private ExtractableResponse<Response> 투표를_진행한다(String accessToken, String location,
                                                   List<PollItemRequest> pollItemRequest) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .body(pollItemRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(location)
                .then().log().all().extract();
    }

    private String 투표를_생성한_뒤_투표_URL을_받는다(String accessToken, PollCreateRequest request) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");
    }

    private ExtractableResponse<Response> 투표를_마감한다(String accessToken, String location) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .patch(location + "/close")
                .then().log().all().extract();
    }
}
