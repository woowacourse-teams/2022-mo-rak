package com.morak.back.performance.support;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.post;

import com.morak.back.AuthSupporter;
import com.morak.back.team.application.dto.TeamCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TeamMemberRequestSupport {

    public static ExtractableResponse<Response> 그룹_생성을_요청한다(TeamCreateRequest request, String token) {
        return post("/api/groups", request, toHeader(token));
    }

    public static String 그룹_생성_요청_후_위치를_가져온다(TeamCreateRequest request, String token) {
        return 그룹_생성을_요청한다(request, token).header("Location");
    }

    public static ExtractableResponse<Response> 그룹_초대코드_생성을_요청한다(String location, String token) {
        return post(location + "/invitation", "", toHeader(token));
    }

    public static String 그룹_초대코드_생성_요청_후_위치를_가져온다(String location, String token) {
        return 그룹_초대코드_생성을_요청한다(location, token).header("Location");
    }

    public static ExtractableResponse<Response> 그룹_참가_여부_조회를_요청한다(String teamInvitationLocation, String token) {
        return get(teamInvitationLocation, toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_참가를_요청한다(String teamInvitationLocation, String otherToken) {
        return post(teamInvitationLocation, "", AuthSupporter.toHeader(otherToken));
    }

    public static ExtractableResponse<Response> 그룹_목록_조회를_요청한다(String token) {
        return get("/api/groups", AuthSupporter.toHeader(token));
    }

    public static ExtractableResponse<Response> 그룹_멤버_목록_조회를_요청한다(String token, String teamLocation) {
        return get(teamLocation + "/members", AuthSupporter.toHeader(token));
    }

    public static ExtractableResponse<Response> 기본_그룹_조회를_요청한다(String otherToken) {
        return get("/api/groups/default", toHeader(otherToken));
    }

    public static ExtractableResponse<Response> 그룹_탈퇴를_요청한다(String teamCode, String token) {
        return delete("/api/groups/out/" + teamCode, toHeader(token));
    }

    public static String extractTeamCodeFromLocation(String teamLocation) {
        return teamLocation.split("/")[3];
    }
}
