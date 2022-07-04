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
        PollCreateRequest request = new PollCreateRequest("투표 제목", 1, false, LocalDateTime.now(), List.of("항목1", "항목2"));
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(request).contentType(MediaType.APPLICATION_JSON_VALUE).post("/polls")
            .then().log().all().extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}