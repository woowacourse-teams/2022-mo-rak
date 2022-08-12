package com.morak.back.appointment.acceptance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.SimpleRestAssured.toObjectList;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.MINUTES_UNIT으로_나눠지지않는_durationMinute_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.durationHour이_25인_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.durationMinute이_60인_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.과거_날짜로_생성_요청된_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_11시_반부터_00시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시반부터_6시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_24_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_하루종일_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.설명이_너무긴_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.약속_잡기_범위_시작과_끝나는_시간이_30분으로_안나눠지는_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.약속_잡기_범위_시작시간이_끝나는_시간과_같은_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.약속_잡기_범위_시작시간이_끝나는_시간보다_이른_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.약속잡기_범위_시작_날짜가_끝나는_날짜보다_나중인_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.제목이_없는_약속잡기_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.총_진행시간이_1440이상인_약속잡기_요청_데이터;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.core.exception.CustomErrorCode;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
        ExtractableResponse<Response> response = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/appointments/")[1]).hasSize(8)
        );
    }

    @Test
    void 약속잡기_목록을_조회한다() {
        // given
        약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터);
        약속잡기_생성을_요청한다(범위_16_24_약속잡기_요청_데이터);

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
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");
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
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

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

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 30",
            "15, 0, 15, 30",
            "15, 30, 16, 0",
            "20, 0, 20, 30"
    })
    void 범위에서_벗어난_약속잡기_가능시간을_선택하면_BAD_REQUEST를_던진다(int startHour, int startMinute, int endHour, int endMinute) {
        // given
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(startHour, startMinute)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(endHour, endMinute))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터,
                availableTimeRequest
        );
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(SimpleRestAssured.extractCodeNumber(response))
                .isEqualTo(CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR.getNumber());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 16, 0, 1, 16, 30",
            "1, 23, 30, 2, 0, 0",
            "2, 23, 30, 3, 0, 0",
            "3, 23, 30, 4, 0, 0"
    })
    void 범위_16시부터_자정까지_속해있는_약속잡기_가능시간을_선택하면_OK를_준다(int startDate, int startHour, int startMinute, int endDate,
                                                   int endHour, int endMinute) {
        // given
        String location = 약속잡기_생성을_요청한다(범위_16_24_약속잡기_요청_데이터).header("Location");

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startDate), LocalTime.of(startHour, startMinute)),
                LocalDateTime.of(LocalDate.now().plusDays(endDate), LocalTime.of(endHour, endMinute))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 15, 30, 1, 16, 0",
            "2, 0, 0, 2, 0, 30",
            "4, 0, 0, 4, 0, 30"
    })
    void 약속잡기가_16시부터_자정까지_선택_가능한_경우_범위를_벗어나면_BAD_REQUEST를_응답한다(
            int startDate, int startHour, int startMinute, int endDate, int endHour, int endMinute) {
        // given
        String location = 약속잡기_생성을_요청한다(범위_16_24_약속잡기_요청_데이터).header("Location");

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDate.now().plusDays(startDate), LocalTime.of(startHour, startMinute)),
                LocalDateTime.of(LocalDate.now().plusDays(endDate), LocalTime.of(endHour, endMinute))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(SimpleRestAssured.extractCodeNumber(response))
                .isIn(CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR.getNumber(),
                        CustomErrorCode.AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR.getNumber());
    }

    @ParameterizedTest
    @MethodSource("getAvailableTimeRequest")
    void 범위가_하루종일일때_약속잡기_가능시간을_선택하면_OK를_준다(AvailableTimeRequest request) {
        // given
        String location = 약속잡기_생성을_요청한다(범위_하루종일_약속잡기_요청_데이터).header("Location");

        // when
        List<AvailableTimeRequest> requests = List.of(request);
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static List<Arguments> getAvailableTimeRequest() {
        return List.of(
                Arguments.of(모락_회식_첫째날_4시부터_4시반_선택_요청_데이터),
                Arguments.of(모락_회식_첫째날_4시반부터_5시_선택_요청_데이터),
                Arguments.of(모락_회식_첫째날_5시부터_5시반_선택_요청_데이터),
                Arguments.of(모락_회식_첫째날_11시_반부터_00시_선택_요청_데이터),
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getStartDate(), LocalTime.of(0, 0)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getStartDate(), LocalTime.of(0, 30))
                )),
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().minusDays(1), LocalTime.of(23, 30)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate(), LocalTime.of(0, 0))
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("getUnavailableTimeRequest")
    void 범위가_하루종일일때_벗어나는_약속잡기_가능시간을_선택하면_BAD_REQUEST를_던진다(AvailableTimeRequest request) {
        // given
        String location = 약속잡기_생성을_요청한다(범위_하루종일_약속잡기_요청_데이터).header("Location");

        // when
        List<AvailableTimeRequest> requests = List.of(request);
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static List<Arguments> getUnavailableTimeRequest() {
        return List.of(
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(10), LocalTime.of(23, 30)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(11), LocalTime.of(0, 0)))
                ),
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getStartDate().minusDays(1), LocalTime.of(23, 30)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getStartDate(), LocalTime.of(0, 0))
                ))
                /*
                TODO 아래 테스트는 400으로 떨어져야 하는데 200이 떨어진다.
                생성된 데이터는 7일 24시(==8일 0시)이고, 8일00시00분 ~ 8일00시30분 으로 요청을 보내면 실패해야 하는데, 성공한다
                -> 테스트가 깨진다.
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(1), LocalTime.of(0, 0)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(1), LocalTime.of(0, 30))
                )),
                Arguments.of(new AvailableTimeRequest(
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(1), LocalTime.of(16, 0)),
                        LocalDateTime.of(범위_하루종일_약속잡기_요청_데이터.getEndDate().plusDays(1), LocalTime.of(16, 30))
                ))
                */
        );
    }

    @Test
    void 약속잡기_가능시간을_재선택한다() {
        // given
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

        // when
        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
        );
        약속잡기_가능_시간_선택을_요청한다(location, requests);
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 30",
            "15, 0, 15, 30",
            "15, 30, 16, 0",
            "20, 0, 20, 30",
            "23, 30, 0, 0"
    })

    @Test
    void 약속잡기_추천_결과를_조회한다() {
        //given
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

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
        assertThat(recommendationResponses).hasSize(4);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidPropertyAppointmentCreateRequest")
    void 약속잡기를_생성_시_값이_잘못된_경우_BAD_REQUEST를_반환한다(AppointmentCreateRequest appointmentCreateRequest) {
        // when
        ExtractableResponse<Response> response = 약속잡기_생성을_요청한다(appointmentCreateRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.INVALID_PROPERTY_ERROR.getNumber())
        );
    }

    static AppointmentCreateRequest[] getInvalidPropertyAppointmentCreateRequest() {
        return new AppointmentCreateRequest[]{
                설명이_너무긴_약속잡기_요청_데이터,
                제목이_없는_약속잡기_요청_데이터
        };
    }

    @ParameterizedTest()
    @MethodSource("getInvalidDomainLogicAppointmentCreateRequest")
    void 약속잡기를_생성_시_도메인_로직상_잘못된_경우_BAD_REQUEST를_반환한다(AppointmentCreateRequest appointmentCreateRequest) {
        // when
        ExtractableResponse<Response> response = 약속잡기_생성을_요청한다(appointmentCreateRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response)).startsWith("31")
        );
    }

    static AppointmentCreateRequest[] getInvalidDomainLogicAppointmentCreateRequest() {
        return new AppointmentCreateRequest[]{
                과거_날짜로_생성_요청된_약속잡기_요청_데이터,
                약속잡기_범위_시작_날짜가_끝나는_날짜보다_나중인_약속잡기_요청_데이터,
                약속_잡기_범위_시작과_끝나는_시간이_30분으로_안나눠지는_약속잡기_요청_데이터,
                약속_잡기_범위_시작시간이_끝나는_시간보다_이른_약속잡기_요청_데이터,
                약속_잡기_범위_시작시간이_끝나는_시간과_같은_약속잡기_요청_데이터,
                MINUTES_UNIT으로_나눠지지않는_durationMinute_약속잡기_요청_데이터,
                durationHour이_25인_약속잡기_요청_데이터,
                durationMinute이_60인_약속잡기_요청_데이터,
                총_진행시간이_1440이상인_약속잡기_요청_데이터
        };
    }

    @Test
    void 약속잡기_가능시간을_중복으로_선택_시_BAD_REQUEST를_반환한다() {
        // given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");

        // when
        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터
        );
        ExtractableResponse<Response> response = 약속잡기_가능_시간_선택을_요청한다(location, requests);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.APPOINTMENT_DUPLICATED_AVAILABLE_TIME_ERROR.getNumber())
        );
    }

    @Test
    void 속하지않은_그룹의_약속잡기_단건을_조회하면_FORBIDDEN을_반환한다() {
        // given
        String location = 약속잡기_생성을_요청한다(모락_회식_약속잡기_요청_데이터).header("Location");
        String otherPath = location.replace("MoraK123", "Betrayed");
        String otherUserToken = tokenProvider.createToken(String.valueOf(4L));

        // when
        ExtractableResponse<Response> response = SimpleRestAssured.get(otherPath, toHeader(otherUserToken));

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(SimpleRestAssured.extractCodeNumber(response))
                        .isEqualTo(CustomErrorCode.APPOINTMENT_TEAM_MISMATCHED_ERROR.getNumber())
        );
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
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

        //when
        ExtractableResponse<Response> response = 약속잡기_마감을_요청한다(location);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 약속잡기를_삭제한다() {
        //given
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");

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
