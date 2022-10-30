package com.morak.back.performance.support;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;

import com.morak.back.role.application.dto.RoleNameEditRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RoleRequestSupport {

    public static ExtractableResponse<Response> 역할_이름_목록_조회를_요청한다(String teamLocation, String token) {
        return get(teamLocation + "/roles/names", toHeader(token));
    }

    public static ExtractableResponse<Response> 역할_이름_목록_수정을_요청한다(String teamLocation, String token, RoleNameEditRequest request) {
        return put(teamLocation + "/roles/names", request, toHeader(token));
    }

    public static ExtractableResponse<Response> 역할_매칭을_요청한다(String teamLocation, String token) {
        return post(teamLocation + "/roles", toHeader(token));
    }

    public static ExtractableResponse<Response> 역할_히스토를_조회를_요청한다(String teamLocation, String token) {
        return get(teamLocation + "/roles/histories", toHeader(token));
    }
}
