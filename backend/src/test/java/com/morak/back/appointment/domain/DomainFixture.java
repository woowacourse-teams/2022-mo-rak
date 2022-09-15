package com.morak.back.appointment.domain;

import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.appointment.domain.appointment.timeconditions.durationtime.DurationTime;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DomainFixture {


    public static final LocalDateTime 시작_시간 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0));

    public static final LocalDateTime 자정_시간 = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(0, 0));
    public static final LocalDateTime 끝_시간 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0));


    public static final DurationTime 두시간_일정 = DurationTime.of(2, 0, 30);
    public static final DurationTime 한시간_일정 = DurationTime.of(1, 0, 30);


    public static final Member 에덴 = new Member(1L, "oauth-id1", "eden", "eden-profile.com");

    public static final Member 까라 = new Member(2L, "oauth-id2", "kara", "kara-profile.com");

    public static final Member 리엘 = new Member(3L, "oauth-id3", "ellie", "ellie-profile.com");

    public static final Appointment 약속잡기_회식_날짜 = Appointment.builder()
            .host(new Member())
            .team(new Team())
            .title("스터디 회의 날짜 정하기")
            .description("필참!!")
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(5))
            .startTime(LocalTime.of(14, 0))
            .endTime(LocalTime.of(20, 0))
            .durationHours(1)
            .durationMinutes(0)
            .closedAt(LocalDateTime.now().plusDays(1))
            .code(Code.generate(length -> "ABCD1234"))
            .times(new LocalTimes())
            .build();
}
