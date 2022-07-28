package com.morak.back.appointment.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static com.morak.back.appointment.AppointmentFixture.모락_스터디_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentFixture.모락_회식_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentFixture.모락_회식_첫째날_5시반부터_6시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.application.TokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class AppointmentAcceptanceTest extends AcceptanceTest {

    private static final String APPOINTMENT_BASE_PATH = "/api/groups/MoraK123/appointments";

    private String accessToken;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
        accessToken = tokenProvider.createToken(String.valueOf(1L));
    }

    @Test
    void 약속잡기를_생성한다() {
        // when
        ExtractableResponse<Response> response = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/appointments/")[1]).hasSize(8)
        );
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given
        약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터);
        약속잡기_생성을_요청한다(모락_스터디_약속잡기_요청_데이터);

        // when
        ExtractableResponse<Response> response = 약속잡기_목록_조회를_요청한다();
        List<AppointmentAllResponse> appointmentAllResponses = toObjectList(response, AppointmentAllResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(appointmentAllResponses).hasSize(3)
        );
    }

    @Test
    void 약속잡기_단건을_조회한다() {
        // given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        // when
        ExtractableResponse<Response> response = 약속잡기_단건_조회를_요청한다(location);
        AppointmentResponse appointmentResponse = response.as(AppointmentResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(appointmentResponse.getCode()).isNotNull(),
                () -> assertThat(appointmentResponse.getTitle()).isEqualTo("모락 회식 날짜 및 시간")
        );
    }

    @Test
    void 약속잡기_가능시간을_선택한다() {
        // given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        // when
        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
        );
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 약속잡기_추천_결과를_조회한다() {
        //given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
        );
        약속잡기_가능_시간_선택을_요청한다(location, requests);
        String accessToken2 = tokenProvider.createToken(String.valueOf(2L));
        List<AvailableTimeRequest> requests2 = List.of(
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터,
                모락_회식_첫째날_5시반부터_6시_선택_요청_데이터
        );
        두번쨰_멤버가_약속잡기_가능_시간_선택을_요청한다(location, requests2, accessToken2);

        //when
        ExtractableResponse<Response> response = 약속잡기_가능_시간_추천_결과_조회를_요청한다(location);
        List<RecommendationResponse> recommendationResponses = toObjectList(response, RecommendationResponse.class);

        //then
        assertThat(recommendationResponses).hasSize(2);
    }

    private ExtractableResponse<Response> 약속잡기_가능_시간_추천_결과_조회를_요청한다(String location) {
        return get(location + "/recommendation", toHeader(accessToken));
    }

    private void 두번쨰_멤버가_약속잡기_가능_시간_선택을_요청한다(String location, List<AvailableTimeRequest> requests,
                                             String accessToken) {
        put(location, requests, toHeader(accessToken));
    }

    @Test
    void 약속잡기를_마감한다() {
        //given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        //when
        ExtractableResponse<Response> response = 약속잡기_마감을_요청한다(location);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 약속잡기를_삭제한다() {
        //given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        //when
        ExtractableResponse<Response> response = 약속잡기_삭제를_요청한다(location);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 약속잡기_생성을_요청한다(AppointmentCreateRequest request) {
        return SimpleRestAssured.post(APPOINTMENT_BASE_PATH, request,
                toHeader(accessToken));
    }

    private ExtractableResponse<Response> 약속잡기_목록_조회를_요청한다() {
        return SimpleRestAssured.get(APPOINTMENT_BASE_PATH, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 약속잡기_단건_조회를_요청한다(String location) {
        return SimpleRestAssured.get(location, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 약속잡기_가능_시간_선택을_요청한다(String location, List<AvailableTimeRequest> requests) {
        return put(location, requests, toHeader(accessToken));
    }

    private ExtractableResponse<Response> 약속잡기_마감을_요청한다(String location) {
        return patch(location + "/close", toHeader(accessToken));
    }

    private ExtractableResponse<Response> 약속잡기_삭제를_요청한다(String location) {
        return delete(location, toHeader(accessToken));
    }
}
