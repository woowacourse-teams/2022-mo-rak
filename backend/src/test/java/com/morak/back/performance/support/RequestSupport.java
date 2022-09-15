package com.morak.back.performance.support;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.post;
import static com.morak.back.SimpleRestAssured.put;

import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResultRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class RequestSupport {

    public static ExtractableResponse<Response> 약속잡기_생성을_요청한다(String teamLocation, AppointmentCreateRequest request,
                                                              String token) {
        return SimpleRestAssured.post(teamLocation + "/appointments", request, toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_목록_조회를_요청한다(String teamLocation, String token) {
        return SimpleRestAssured.get(teamLocation + "/appointments", toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_단건_조회를_요청한다(String location, String token) {
        return SimpleRestAssured.get(location, toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_가능_시간_선택을_요청한다(String location,
                                                                    List<AvailableTimeRequest> requests, String token) {
        return put(location, requests, toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_가능_시간_추천_결과_조회를_요청한다(String location, String token) {
        return get(location + "/recommendation", toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_마감을_요청한다(String location, String token) {
        return patch(location + "/close", toHeader(token));
    }

    public static ExtractableResponse<Response> 약속잡기_삭제를_요청한다(String location, String token) {
        return delete(location, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_생성을_요청한다(String teamLocation, PollCreateRequest request,
                                                            String token) {
        return post(teamLocation + "/polls", request, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_선택항목_조회를_요청한다(String pollLocation, String token) {
        return get(pollLocation + "/items", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_진행을_요청한다(String location, List<PollResultRequest> requests,
                                                            String token) {
        return put(location, requests, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_목록_조회를_요청한다(String teamLocation, String token) {
        return get(teamLocation + "/polls", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_마감을_요청한다(String location, String token) {
        return patch(location + "/close", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_단건_조회를_요청한다(String location, String token) {
        return get(location, toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_결과_조회를_요청한다(String location, String token) {
        return get(location + "/result", toHeader(token));
    }

    public static ExtractableResponse<Response> 투표_삭제를_요청한다(String location, String token) {
        return delete(location, toHeader(token));
    }
}
