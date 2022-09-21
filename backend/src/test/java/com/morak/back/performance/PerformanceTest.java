package com.morak.back.performance;

import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;
import static com.morak.back.performance.Fixture.APPOINTMENT_SIZE;
import static com.morak.back.performance.Fixture.APPOINTMENT_SIZE_PER_TEAM;
import static com.morak.back.performance.Fixture.JOIN_TEAM_SIZE;
import static com.morak.back.performance.Fixture.MEMBER_SIZE;
import static com.morak.back.performance.Fixture.POLL_ITEM_SIZE;
import static com.morak.back.performance.Fixture.POLL_ITEM_SIZE_PER_POLL;
import static com.morak.back.performance.Fixture.POLL_SIZE;
import static com.morak.back.performance.Fixture.POLL_SIZE_PER_TEAM;
import static com.morak.back.performance.Fixture.TEAM_ID1_LOCATION;
import static com.morak.back.performance.Fixture.TEAM_ID2_LOCATION;
import static com.morak.back.performance.Fixture.TEAM_SIZE;
import static com.morak.back.performance.Fixture.기명_다중선택_항목2개_투표_생성_요청_데이터;
import static com.morak.back.performance.Fixture.약속잡기_가능시간_3개_선택_요청_데이터;
import static com.morak.back.performance.Fixture.투표_결과_요청_데이터;
import static com.morak.back.performance.Fixture.팀_생성_요청_데이터;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_가능_시간_선택을_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_가능_시간_추천_결과_조회를_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_단건_조회를_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_마감을_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_목록_조회를_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_삭제를_요청한다;
import static com.morak.back.performance.support.AppointmentRequestSupport.약속잡기_생성_요청_후_위치를_가져온다;
import static com.morak.back.performance.support.PollRequestSupport.투표_결과_조회를_요청한다;
import static com.morak.back.performance.support.PollRequestSupport.투표_단건_조회를_요청한다;
import static com.morak.back.performance.support.PollRequestSupport.투표_마감을_요청한다;
import static com.morak.back.performance.support.PollRequestSupport.투표_목록_조회를_요청한다;
import static com.morak.back.performance.support.PollRequestSupport.투표_삭제를_요청한다;
import static com.morak.back.performance.support.PollRequestSupport.투표_생성_요청_후_위치를_가져온다;
import static com.morak.back.performance.support.PollRequestSupport.투표_선택항목_조회_요청_후_바디를_가져온다;
import static com.morak.back.performance.support.PollRequestSupport.투표_진행을_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.extractTeamCodeFromLocation;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_멤버_목록_조회를_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_목록_조회를_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_생성_요청_후_위치를_가져온다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_참가_여부_조회를_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_참가를_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_초대코드_생성_요청_후_위치를_가져온다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.그룹_탈퇴를_요청한다;
import static com.morak.back.performance.support.TeamMemberRequestSupport.기본_그룹_조회를_요청한다;

import com.morak.back.auth.application.TokenProvider;
import com.morak.back.performance.support.AppointmentDummySupport;
import com.morak.back.performance.support.PollDummySupport;
import com.morak.back.performance.support.TeamMemberDummySupport;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"})
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'performance'}", loadContext = true)
public class PerformanceTest {

    private static final Logger LOG = LoggerFactory.getLogger("PERFORMANCE");

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TeamMemberDummySupport teamMemberDummySupport;

    @Autowired
    private AppointmentDummySupport appointmentDummySupport;

    @Autowired
    private PollDummySupport pollDummySupport;

    @LocalServerPort
    int port;

    private String member1Token;
    private String member2Token;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        member1Token = tokenProvider.createToken(String.valueOf(1L));
        member2Token = tokenProvider.createToken(String.valueOf(2L));

        더미데이터를_추가한다();
    }

    @Test
    void 성능을_테스트한다() {
        LOG.info("====== 성능 테스트 start ======");
        팀_멤버_API의_성능을_테스트한다();
        약속잡기_API의_성능을_테스트한다();
        투표_API의_성능을_테스트한다();
    }

    private void 더미데이터를_추가한다() {
        LOG.info("====== 더미 데이터 추가 start ======");
        long startTime = System.currentTimeMillis();

        teamMemberDummySupport.멤버_더미데이터를_추가한다(MEMBER_SIZE);
        teamMemberDummySupport.팀_더미데이터를_추가한다(TEAM_SIZE);
        teamMemberDummySupport.팀_멤버_더미데이터를_추가한다(MEMBER_SIZE, TEAM_SIZE, JOIN_TEAM_SIZE);

        appointmentDummySupport.약속잡기_더미데이터를_추가한다(TEAM_SIZE, APPOINTMENT_SIZE_PER_TEAM);
        appointmentDummySupport.약속잡기_선택가능시간_더미데이터를_추가한다(APPOINTMENT_SIZE);

        pollDummySupport.투표_더미데이터를_추가한다(TEAM_SIZE, POLL_SIZE_PER_TEAM);
        pollDummySupport.투표_선택항목_더미데이터를_추가한다(POLL_SIZE, POLL_ITEM_SIZE_PER_POLL);
        pollDummySupport.투표_선택결과_더미데이터를_추가한다(POLL_ITEM_SIZE);

        double timeOfInsultDummies = (System.currentTimeMillis() - startTime) / 1_000.0;
        LOG.info(String.format("더미 데이터 추가 시간: %f", timeOfInsultDummies));
    }

    private void 팀_멤버_API의_성능을_테스트한다() {
        LOG.info("[팀 & 멤버 성능 테스트]");
        String location = 그룹_생성_요청_후_위치를_가져온다(팀_생성_요청_데이터, member1Token);
        String invitationLocation = 그룹_초대코드_생성_요청_후_위치를_가져온다(location, member1Token);
        그룹_참가_여부_조회를_요청한다(invitationLocation, member1Token);
        그룹_참가를_요청한다(invitationLocation, member2Token);
        그룹_목록_조회를_요청한다(member1Token);
        그룹_멤버_목록_조회를_요청한다(member1Token, location);
        기본_그룹_조회를_요청한다(member2Token);
        그룹_탈퇴를_요청한다(extractTeamCodeFromLocation(location), member2Token);
    }

    private void 약속잡기_API의_성능을_테스트한다() {
        LOG.info("[약속잡기 성능 테스트]");
        String location = 약속잡기_생성_요청_후_위치를_가져온다(TEAM_ID1_LOCATION, 범위_16_20_약속잡기_요청_데이터, member1Token);
        약속잡기_목록_조회를_요청한다(TEAM_ID1_LOCATION, member1Token);
        약속잡기_단건_조회를_요청한다(location, member1Token);
        약속잡기_가능_시간_선택을_요청한다(location, 약속잡기_가능시간_3개_선택_요청_데이터, member1Token);
        약속잡기_가능_시간_선택을_요청한다(location, 약속잡기_가능시간_3개_선택_요청_데이터, member1Token); // 재선택
        약속잡기_가능_시간_선택을_요청한다(location, 약속잡기_가능시간_3개_선택_요청_데이터, member2Token); // 다른 멤버도 선택
        약속잡기_가능_시간_추천_결과_조회를_요청한다(location, member1Token);
        약속잡기_마감을_요청한다(location, member1Token);
        약속잡기_삭제를_요청한다(location, member1Token);
    }

    private void 투표_API의_성능을_테스트한다() {
        LOG.info("[투표 성능 테스트]");
        투표_목록_조회를_요청한다(TEAM_ID2_LOCATION, member1Token);
        String location = 투표_생성_요청_후_위치를_가져온다(TEAM_ID2_LOCATION, 기명_다중선택_항목2개_투표_생성_요청_데이터, member1Token);
        투표_단건_조회를_요청한다(location, member1Token);
        List<PollItemResponse> pollItemResponses = 투표_선택항목_조회_요청_후_바디를_가져온다(location, member1Token);
        List<PollResultRequest> 투표_결과_2개_요청_데이터 = makePollResultRequests(pollItemResponses);
        투표_진행을_요청한다(location, 투표_결과_2개_요청_데이터, member1Token);
        투표_진행을_요청한다(location, 투표_결과_2개_요청_데이터, member1Token); // 재투표
        투표_진행을_요청한다(location, 투표_결과_2개_요청_데이터, member2Token); // 다른 멤버도 투표
        투표_결과_조회를_요청한다(location, member1Token);
        투표_마감을_요청한다(location, member1Token);
        투표_삭제를_요청한다(location, member1Token);
    }

    private List<PollResultRequest> makePollResultRequests(List<PollItemResponse> pollItemResponses) {
        return pollItemResponses.stream()
                .map(response -> 투표_결과_요청_데이터(response.getId()))
                .collect(Collectors.toList());
    }
}
