package com.morak.back.auth.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.post;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.auth.application.FakeOAuthClient;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.core.exception.CustomErrorCode;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class OAuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    void 유효하지_않은_사용자_코드로_로그인하는_경우_실패_응답() {
        // given
        String code = FakeOAuthClient.FAILURE;
        SigninRequest request = new SigninRequest(code);

        // when
        ExtractableResponse<Response> response = 로그인을_요청한다(request);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.GITHUB_AUTHORIZATION_ERROR.getNumber())
        );
    }

    @Test
    void 자기자신을_조회한다() {
        // given
        String token = tokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 자기자신_조회를_요청한다(token);

        // then
        MemberResponse memberResponse = response.as(MemberResponse.class);
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberResponse.getId()).isEqualTo(1)
        );
    }

    private ExtractableResponse<Response> 로그인을_요청한다(SigninRequest request) {
        return post("/api/auth/signin", request);
    }

    private ExtractableResponse<Response> 자기자신_조회를_요청한다(String token) {
        return SimpleRestAssured.get("/api/auth/me", toHeader(token));
    }
}
