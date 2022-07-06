package com.morak.back.poll.acceptance;

import static org.assertj.core.api.Assertions.*;

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
}