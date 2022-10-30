package com.morak.back.performance;

import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;

import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import java.time.LocalDateTime;
import java.util.List;

public class Fixture {

    public static final int MEMBER_SIZE = 500;
    public static final int TEAM_SIZE = 1000;
    public static final int JOIN_TEAM_SIZE = 4;
    public static final int APPOINTMENT_SIZE_PER_TEAM = 30;
    public static final int APPOINTMENT_SIZE = TEAM_SIZE * APPOINTMENT_SIZE_PER_TEAM;
    public static final int POLL_SIZE_PER_TEAM = 30;
    public static final int POLL_SIZE = TEAM_SIZE * POLL_SIZE_PER_TEAM;
    public static final int POLL_ITEM_SIZE_PER_POLL = 3;
    public static final int POLL_ITEM_SIZE = POLL_SIZE * POLL_ITEM_SIZE_PER_POLL;

//    public static final Member MEMBER_ID1 = Member.builder().id(1L).build();
//    public static final Member MEMBER_ID2 = Member.builder().id(2L).build();

    public static final String TEAM_ID1_LOCATION = "/api/groups/00000001";
    public static final String TEAM_ID2_LOCATION = "/api/groups/00000002";
    public static final TeamCreateRequest 팀_생성_요청_데이터 = new TeamCreateRequest("투표 제목");

    public static final List<AvailableTimeRequest> 약속잡기_가능시간_3개_선택_요청_데이터 = List.of(
            모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
            모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
            모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
    );

    public static final PollCreateRequest 기명_다중선택_항목2개_투표_생성_요청_데이터 = new PollCreateRequest(
            "투표 제목",
            2,
            false,
            LocalDateTime.now().plusDays(1),
            List.of("항목1", "항목2")
    );

    public static PollResultRequest 투표_결과_요청_데이터(long pollItemId) {
        return new PollResultRequest(pollItemId, "투표 이유입니당!");
    }
}
