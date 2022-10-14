package com.morak.back.role.acceptance;


import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.toObject;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.ui.RoleController;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class RoleAcceptanceTest extends AcceptanceTest {

    private String token;

    @Autowired
    private TokenProvider tokenProvider;

    // -- A
    @Test
    void 투표_목록을_조회한다() {
        // given
        token = tokenProvider.createToken(String.valueOf(1L));

        // when

        ExtractableResponse<Response> response = get("/api/groups/code1234/roles/names", toHeader(token));
        RoleNameResponses roleNameResponses = toObject(response, RoleNameResponses.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(roleNameResponses.getRoles()).containsExactly("반장", "청소부")
        );
    }


    // -- B



    // -- C



    // -- D


}
