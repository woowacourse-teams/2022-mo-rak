package com.morak.back.role.acceptance;


import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_생성을_요청한다;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class RoleAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TokenProvider tokenProvider;

    private String token;

    // -- A

    // -- B

    @BeforeEach
    public void setUp() {
        super.setUp();
        token = tokenProvider.createToken(String.valueOf(1L));
    }

    @Test
    void 그룹_생성시_역할_정하기를_생성한다(@Autowired RoleRepository roleRepository)
            throws InterruptedException {
        // given
        ExtractableResponse<Response> albur = 그룹_생성을_요청한다(new TeamCreateRequest("albur"), token);

        String[] splitLocation = albur.header("Location").split("/");
        String groupCode = splitLocation[splitLocation.length - 1];


        // when
        Optional<Role> optionalRole = roleRepository.findByTeamCode(groupCode);

        // then
        assertThat(optionalRole).isPresent();
    }

    @Test
    void 역할정하기_이름_목록을_수정한다() {
        // given
        RoleNameEditRequest request = new RoleNameEditRequest(List.of("서기", "타임키퍼"));

        // when
        ExtractableResponse<Response> response = put("/api/groups/MoraK123/roles/names", request, toHeader(token));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    // -- C

    // -- D


}
