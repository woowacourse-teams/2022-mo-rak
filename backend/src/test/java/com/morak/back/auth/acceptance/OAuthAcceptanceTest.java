package com.morak.back.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.ui.dto.SigninRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OAuthAcceptanceTest extends AcceptanceTest {

    @Test
    public void 유효하지_않은_사용자_코드로_로그인하는_경우_실패_응답() {
        // given
        String code = "test-code";

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new SigninRequest(code))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/api/auth/signin")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
