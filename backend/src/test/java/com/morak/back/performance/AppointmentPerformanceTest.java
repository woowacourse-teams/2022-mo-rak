package com.morak.back.performance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;

import com.morak.back.AcceptanceTest;
import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.domain.AvailableTimeRepository;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.team.domain.Team;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
public class AppointmentPerformanceTest extends AcceptanceTest {

    private static final String APPOINTMENT_BASE_PATH = "/api/groups/MoraK123/appointments";

    private static final Logger log = LoggerFactory.getLogger("PERFORMANCE");

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    private String accessToken;
    private Team team;
    private Member member;

    @BeforeEach
    public void setUp() {
        super.setUp();
        accessToken = tokenProvider.createToken(String.valueOf(1L));

        team = Team.builder()
                .id(1L)
                .build();
        member = Member.builder()
                .id(1L)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 100, 1000, 5000, 10_000})
    void 더미데이터를_추가하고_약속잡기_성능을_테스트한다(int size) {
        // given
        insertDummyAppointments(size);
        String location = insertDummyAppointment();

        // when
        log.info(String.format("[약속잡기] 더미 데이터 개수: %d", size));
        약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터);
        약속잡기_목록_조회를_요청한다();
        약속잡기_단건_조회를_요청한다(location);
        List<AvailableTimeRequest> requests = List.of(
                모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
        );
        약속잡기_가능_시간_선택을_요청한다(location, requests);
        약속잡기_마감을_요청한다(location);
        약속잡기_삭제를_요청한다(location);
    }

    private void insertDummyAppointments(int size) {
        for (int i = 0; i < size; i++) {
            insertDummyAppointment();
        }
    }

    private String insertDummyAppointment() {
        Appointment dummyAppointment = Appointment.builder()
                .team(team)
                .host(member)
                .title("모락 회식 언제가 좋아?")
                .description("필참!!")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(1)
                .durationMinutes(0)
                .code(Code.generate(new RandomCodeGenerator()))
                .closedAt(LocalDateTime.now().plusDays(1))
                .build();

        appointmentRepository.save(dummyAppointment);

        insertDummyAvailableTimes(dummyAppointment);

        return APPOINTMENT_BASE_PATH + "/" + dummyAppointment.getCode();
    }

    private void insertDummyAvailableTimes(Appointment dummyAppointment) {
        List<AvailableTime> dummyAvailableTimes = new ArrayList<>();
        for (int day = 1; day <= 8; day++) {
            for (int hour = 16; hour <= 19; hour++) {
                dummyAvailableTimes.add(
                        AvailableTime.builder()
                                .appointment(dummyAppointment)
                                .member(member)
                                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 0)))
                                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 30)))
                                .build()
                );
                dummyAvailableTimes.add(
                        AvailableTime.builder()
                                .appointment(dummyAppointment)
                                .member(member)
                                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 30)))
                                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour + 1, 0)))
                                .build()
                );
            }
        }

        availableTimeRepository.saveAll(dummyAvailableTimes);
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
