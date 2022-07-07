package com.morak.back.poll.acceptance;

import static org.assertj.core.api.Assertions.*;

import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.morak.back.AcceptanceTest;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class PollAcceptanceTest extends AcceptanceTest {

    @DisplayName("투표를 생성한다.")
    @Test
    void createPoll() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("투표 목록을 조회한다.")
    @Test
    void findPolls() {
        // given & when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
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
        PollCreateRequest createRequest = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        String location = RestAssured.given().log().all()
                .body(createRequest).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");

        List<Long> request = List.of(4L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(location)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("재투표를 진행한다.")
    @Test
    void rePoll() {
        // given
        List<Long> request = List.of(3L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put("/polls/1")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("투표 단건을 조회한다.")
    @Test
    void findPoll() {
        // given & when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get("/polls/1")
                .then().log().all().extract();

        // then
        PollResponse pollResponse = response.body().jsonPath().getObject(".", PollResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollResponse.getTitle()).isEqualTo("test-poll-title"),
                () -> assertThat(pollResponse.getIsHost()).isTrue()
        );
    }

    @DisplayName("투표 선택 항목을 조회한다.")
    @Test
    void findPollItems() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(),
                List.of("항목1", "항목2"));
        String location = RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(location + "/items")
                .then().log().all().extract();

        // then
        List<PollItemResponse> pollItemResponses = response.body().jsonPath().getList(".", PollItemResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getId()).isNotNull(),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1")
        );
    }

    @DisplayName("무기명 투표 결과를 조회한다.")
    @Test
    public void findPollResultsWithAnonymous() {
        // given
        // 투표를 생성한다.
        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, true, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");

        // 투표를 진행한다.
        RestAssured.given().log().all()
                .body(List.of(4, 5))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(location)
                .then().log().all().extract();
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(location + "/result")
                .then().log().all().extract();

        final List<PollItemResultResponse> resultResponses
                = response.body().jsonPath().getList(".", PollItemResultResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(3),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(0),
                () -> assertThat(resultResponses.get(0).getCount()).isEqualTo(1)
        );
    }

    @DisplayName("기명 투표 결과를 조회한다.")
    @Test
    public void findPollResults() {
        // given
        // 투표를 생성한다.
        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");

        // 투표를 진행한다.
        RestAssured.given().log().all()
                .body(List.of(4, 5))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(location)
                .then().log().all().extract();
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(location + "/result")
                .then().log().all().extract();

        final List<PollItemResultResponse> resultResponses
                = response.body().jsonPath().getList(".", PollItemResultResponse.class);
        // then
        Assertions.assertAll(
                () -> assertThat(resultResponses).hasSize(3),
                () -> assertThat(resultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(1).getMembers()).hasSize(1),
                () -> assertThat(resultResponses.get(2).getMembers()).hasSize(0)
        );
    }

    @DisplayName("투표를 삭제한다.")
    @Test
    public void deletePoll() {
        // given
        // 투표를 생성한다.
        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
                List.of("항목1", "항목2", "항목3"));
        String location = RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
                .then().log().all().extract().header("Location");
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .delete(location)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("투표를 마감한다")
    @Test
    void closePoll() {
        // given
        PollCreateRequest request = new PollCreateRequest("투표 제목", 2, false, LocalDateTime.now(),
            List.of("항목1", "항목2", "항목3"));
        String location = RestAssured.given().log().all()
            .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
            .then().log().all().extract().header("Location");
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .patch(location + "/close")
            .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}