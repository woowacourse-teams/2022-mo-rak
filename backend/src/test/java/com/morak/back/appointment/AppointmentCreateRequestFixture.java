package com.morak.back.appointment;

import com.morak.back.core.domain.SystemTime;
import com.morak.back.appointment.application.dto.AppointmentCreateRequest;
import com.morak.back.appointment.application.dto.AvailableTimeRequest;
import com.morak.back.core.domain.FakeSystemTime;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentCreateRequestFixture {

    private static final SystemTime SYSTEM_TIME = new FakeSystemTime();

    public static AppointmentCreateRequest 모락_회식_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 제목이_없는_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            " ",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 과거_날짜로_생성_요청된_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().minusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 약속잡기_범위_시작_날짜가_끝나는_날짜보다_나중인_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().minusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(16, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 약속_잡기_범위_시작과_끝나는_시간이_30분으로_안나눠지는_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(16, 29),
            LocalTime.of(20, 31),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 약속_잡기_범위_시작시간이_끝나는_시간보다_이른_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(19, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 약속_잡기_범위_시작시간이_끝나는_시간과_같은_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(20, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest MINUTES_UNIT으로_나눠지지않는_durationMinute_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(20, 0),
            1,
            1,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest durationHour이_25인_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(20, 0),
            25,
            1,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest durationMinute이_60인_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(20, 0),
            1,
            60,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 총_진행시간이_1440이상인_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(20, 0),
            LocalTime.of(20, 0),
            24,
            30,
            SYSTEM_TIME.now().plusDays(1));

    public static AvailableTimeRequest 모락_회식_첫째날_4시부터_4시반_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(SYSTEM_TIME.now().toLocalDate().plusDays(1), LocalTime.of(16, 0))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_4시반부터_5시_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(SYSTEM_TIME.now().toLocalDate().plusDays(1), LocalTime.of(16, 30))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_5시부터_5시반_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(SYSTEM_TIME.now().toLocalDate().plusDays(1), LocalTime.of(17, 0))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_5시반부터_6시_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(SYSTEM_TIME.now().toLocalDate().plusDays(1), LocalTime.of(17, 30))
    );

    public static AvailableTimeRequest 모락_회식_첫째날_11시_반부터_00시_선택_요청_데이터 = new AvailableTimeRequest(
            LocalDateTime.of(SYSTEM_TIME.now().toLocalDate().plusDays(1), LocalTime.of(23, 30))
    );

    public static AppointmentCreateRequest 범위_하루종일_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(7),
            LocalTime.of(0, 0),
            LocalTime.of(0, 0),
            1,
            30,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 범위_16_24_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "스터디 회의 시간",
            "스터디 진행과 관련된 회의입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(3),
            LocalTime.of(16, 0),
            LocalTime.of(0, 0),
            2,
            0,
            SYSTEM_TIME.now().plusDays(1));

    public static AppointmentCreateRequest 범위_16_20_약속잡기_요청_데이터 = new AppointmentCreateRequest(
            "모락 회식 날짜 및 시간",
            "필참입니다.",
            SYSTEM_TIME.now().toLocalDate().plusDays(1),
            SYSTEM_TIME.now().toLocalDate().plusDays(8),
            LocalTime.of(16, 0),
            LocalTime.of(20, 0),
            1,
            0,
            SYSTEM_TIME.now().plusDays(1));
}
