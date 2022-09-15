package com.morak.back.performance.support;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.put;

import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class AppointmentRequestSupport {

    public static ExtractableResponse<Response> 약속잡기_생성을_요청한다(String teamLocation, AppointmentCreateRequest request,
                                                              String token) {
        return SimpleRestAssured.post(teamLocation + "/appointments", request, toHeader(token));
    }

    public static String 약속잡기_생성_요청_후_위치를_가져온다(String teamLocation, AppointmentCreateRequest request, String token) {
        return 약속잡기_생성을_요청한다(teamLocation, request, token).header("Location");
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
}
