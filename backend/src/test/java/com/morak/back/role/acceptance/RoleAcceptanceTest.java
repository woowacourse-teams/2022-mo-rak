package com.morak.back.role.acceptance;


import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObject;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_초대코드_생성을_요청한다;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RolesResponse;
import com.morak.back.role.domain.Role;
import com.morak.back.role.domain.RoleRepository;
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
    private String teamLocation;

    @BeforeEach
    public void setUp() {
        super.setUp();
        token = tokenProvider.createToken(String.valueOf(1L));
        teamLocation = 그룹_생성을_요청한다(new TeamCreateRequest("그룹"), token).header("Location");
    }

    @Test
    void 역할_목록을_조회한다() {
        // when
        ExtractableResponse<Response> response = 역할_이름들을_조회();
        RoleNameResponses roleNameResponses = toObject(response, RoleNameResponses.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(roleNameResponses.getRoles()).containsExactly("데일리 마스터")
        );
    }

    @Test
    void 그룹_생성시_역할_정하기를_생성한다(@Autowired RoleRepository roleRepository) {
        // given
        String[] splitLocation = teamLocation.split("/");
        String teamCode = splitLocation[splitLocation.length - 1];

        // when
        Optional<Role> optionalRole = roleRepository.findByTeamCode(teamCode);

        // then
        assertThat(optionalRole).isPresent();
    }

    @Test
    void 역할정하기_이름_목록을_수정한다() {
        // given
        RoleNameEditRequest request = new RoleNameEditRequest(List.of("서기", "타임키퍼"));

        // when
        ExtractableResponse<Response> response = 역할정하기_이름_목록_수정(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 역할을_매칭한다() {
        // when
        ExtractableResponse<Response> response = 역할_매칭을_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).contains(teamLocation + "/roles/");
    }

    @Test
    void 역할_히스토리를_조회한다() {
        // given
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        String otherToken = tokenProvider.createToken(String.valueOf(2L));
        그룹_참가를_요청한다(teamInvitationLocation, otherToken);

        역할_매칭을_요청();
        역할_매칭을_요청();

        // when
        ExtractableResponse<Response> response = 역할_히스토리를_조회();
        RolesResponse rolesResponse = toObject(response, RolesResponse.class);

        // then
        assertThat(rolesResponse.getRoles()).hasSize(1);
    }

    private ExtractableResponse<Response> 역할_이름들을_조회() {
        return get(teamLocation + "/roles/names", toHeader(token));
    }

    private ExtractableResponse<Response> 역할정하기_이름_목록_수정(RoleNameEditRequest request) {
        return put(teamLocation + "/roles/names", request, toHeader(token));
    }

    private ExtractableResponse<Response> 역할_매칭을_요청() {
        return post(teamLocation + "/roles", toHeader(token));
    }

    private ExtractableResponse<Response> 역할_히스토리를_조회() {
        return get(teamLocation + "/roles/histories", toHeader(token));
    }
}
