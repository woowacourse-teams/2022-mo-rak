package com.morak.back.performance.support;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObjectList;

import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class PollRequestSupport {

    public static ExtractableResponse<Response> 투표_생성을_요청한다(String teamLocation, PollCreateRequest request,
                                                            String token) {
        return post(teamLocation + "/polls", request, toHeader(token));
    }

    public static String 투표_생성_요청_후_위치를_가져온다(String teamLocation, PollCreateRequest request,
                                             String token) {
        return 투표_생성을_요청한다(teamLocation, request, token).header("Location");
    }

    public static ExtractableResponse<Response> 투표_목록_조회를_요청한다(String teamLocation, String token) {
        return get(teamLocation + "/polls", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_단건_조회를_요청한다(String location, String token) {
        return get(location, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_선택항목_조회를_요청한다(String location, String token) {
        return get(location + "/items", toHeader(token));
    }

    public static List<PollItemResponse> 투표_선택항목_조회_요청_후_바디를_가져온다(String location, String token) {
        return toObjectList(투표_선택항목_조회를_요청한다(location, token), PollItemResponse.class);
    }

    public static ExtractableResponse<Response> 투표_진행을_요청한다(String location, List<PollResultRequest> requests,
                                                            String token) {
        return put(location, requests, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_결과_조회를_요청한다(String location, String token) {
        return get(location + "/result", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_마감을_요청한다(String location, String token) {
        return patch(location + "/close", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_삭제를_요청한다(String location, String token) {
        return delete(location, toHeader(token));
    }
}
