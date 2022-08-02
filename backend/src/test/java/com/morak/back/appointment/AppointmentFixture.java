package com.morak.back.appointment;

import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentFixture {

    public static AppointmentCreateRequest 모락_회식_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(8),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            1,
            0
    );

    public static AvailableTimeRequest 모락_회식_첫째날_4시부터_4시반_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)),
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_4시반부터_5시_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30)),
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 0))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_5시부터_5시반_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 0)),
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 30))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_5시반부터_6시_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 30)),
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0))
    );

    public static AppointmentCreateRequest 모락_스터디_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "스터디 회의 시간",
            "스터디 진행과 관련된 회의입니다.",
            LocalDate.of(2022, 8, 5),
            LocalDate.of(2022, 8, 20),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            2,
            0
    );
}
