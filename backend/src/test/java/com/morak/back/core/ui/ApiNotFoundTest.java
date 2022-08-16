package com.morak.back.core.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.core.exception.CustomErrorCode;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ApiNotFoundTest extends AcceptanceTest {

    @Test
    void 존재하지_않는_API가_요청되면_NOT_FOUND를_응답한다() {
        // given
        String invalidApi = "invalidApi";

        // when
        ExtractableResponse<Response> response = SimpleRestAssured.get(invalidApi);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.API_NOT_FOUND_ERROR.getNumber())
        );
    }

    @Test
    void rest_docs_문서가_요청되면_OK를_응답한다() {
        // given
        String resourceApi = "/docs/index.html";

        // when
        ExtractableResponse<Response> response = SimpleRestAssured.get(resourceApi);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void rest_docs_문서가_아닌_정적파일이_요청되면_NOT_FOUND를_응답한다() {
        // given
        String invalidResourceApi = "/docs/index.htm";

        // when
        ExtractableResponse<Response> response = SimpleRestAssured.get(invalidResourceApi);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.API_NOT_FOUND_ERROR.getNumber())
        );
    }
}
