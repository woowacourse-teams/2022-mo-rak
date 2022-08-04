package com.morak.back.auth.acceptance;

import static com.morak.back.SimpleRestAssured.post;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.ui.dto.SigninRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OAuthAcceptanceTest extends AcceptanceTest {

    @Test
    void 유효하지_않은_사용자_코드로_로그인하는_경우_실패_응답() {
        // given
        String code = "test-code";
        String path = "/api/auth/signin";
        SigninRequest request = new SigninRequest(code);

        // when
        ExtractableResponse<Response> response = 로그인을_요청한다(path, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인을_요청한다(String path, SigninRequest request) {
        return post(path, request);
    }
}
