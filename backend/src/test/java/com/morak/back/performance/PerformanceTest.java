package com.morak.back.performance;

import static com.morak.back.SimpleRestAssured.toObjectList;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;
import static com.morak.back.performance.support.RequestSupport.약속잡기_가능_시간_선택을_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_가능_시간_추천_결과_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_단건_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_마감을_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_목록_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_삭제를_요청한다;
import static com.morak.back.performance.support.RequestSupport.약속잡기_생성을_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_단건_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_마감을_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_목록_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_삭제를_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_생성을_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_선택항목_조회를_요청한다;
import static com.morak.back.performance.support.RequestSupport.투표_진행을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.extractTeamCodeFromLocation;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_멤버_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가_여부_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_초대코드_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_탈퇴를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.기본_그룹_조회를_요청한다;

import com.morak.back.auth.application.TokenProvider;
import com.morak.back.performance.support.AppointmentDummySupport;
import com.morak.back.performance.support.PollDummySupport;
import com.morak.back.performance.support.TeamMemberDummySupport;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"})
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

    private String tokenOfMember1;
    private String tokenOfMember2;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        tokenOfMember1 = tokenProvider.createToken(String.valueOf(1L));
        tokenOfMember2 = tokenProvider.createToken(String.valueOf(2L));

        LOG.info("====== 더미 데이터 추가 start ======");
        long startTime = System.currentTimeMillis();
        더미데이터를_추가한다();
        double timeOfInsultingDummies = (System.currentTimeMillis() - startTime) / 1_000.0;
        LOG.info(String.format("더미 데이터 추가 시간: %f", timeOfInsultingDummies));
    }

    @Test
    void 성능을_테스트한다() {
        LOG.info("====== 성능 테스트 start ======");
        팀_멤버_API의_성능을_테스트한다();
        약속잡기_API의_성능을_테스트한다();
        투표_API의_성능을_테스트한다();
    }

    private void 더미데이터를_추가한다() {
        int memberSize = 500;
        int teamSize = 1000;
        int joinSize = 4;
        int appointmentSizePerTeam = 30;
        int appointmentSize = teamSize * appointmentSizePerTeam;
        int pollSizePerTeam = 30;
        int pollSize = teamSize * pollSizePerTeam;
        int pollItemSizePerPoll = 3;
        int pollItemSize = pollSize * pollItemSizePerPoll;

        teamMemberDummySupport.멤버_더미데이터를_추가한다(memberSize);
        teamMemberDummySupport.팀_더미데이터를_추가한다(teamSize);
        teamMemberDummySupport.팀_멤버_더미데이터를_추가한다(memberSize, teamSize, joinSize);

        appointmentDummySupport.약속잡기_더미데이터를_추가한다(teamSize, appointmentSizePerTeam);
        appointmentDummySupport.약속잡기_선택가능시간_더미데이터를_추가한다(appointmentSize);

        pollDummySupport.투표_더미데이터를_추가한다(teamSize, pollSizePerTeam);
        pollDummySupport.투표_선택항목_더미데이터를_추가한다(pollSize, pollItemSizePerPoll);
        pollDummySupport.투표_선택결과_더미데이터를_추가한다(pollItemSize);
    }

    private void 팀_멤버_API의_성능을_테스트한다() {
        LOG.info("[팀 & 멤버 성능 테스트]");
        TeamCreateRequest request = new TeamCreateRequest("모락팀");
        String teamLocation = 그룹_생성을_요청한다(request, tokenOfMember1).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, tokenOfMember1).header("Location");
        그룹_참가_여부_조회를_요청한다(teamInvitationLocation, tokenOfMember1);
        그룹_참가를_요청한다(teamInvitationLocation, tokenOfMember2);
        그룹_목록_조회를_요청한다(tokenOfMember1);
        그룹_멤버_목록_조회를_요청한다(tokenOfMember1, teamLocation);
        기본_그룹_조회를_요청한다(tokenOfMember2);
        String teamCode = extractTeamCodeFromLocation(teamLocation);
        그룹_탈퇴를_요청한다(teamCode, tokenOfMember2);
    }

    private void 약속잡기_API의_성능을_테스트한다() {
        LOG.info("[약속잡기 성능 테스트]");
        String location = 약속잡기_생성을_요청한다("/api/groups/code1", 범위_16_20_약속잡기_요청_데이터, tokenOfMember1).header("Location");
        약속잡기_목록_조회를_요청한다("/api/groups/code1", tokenOfMember1);
        약속잡기_단건_조회를_요청한다(location, tokenOfMember1);
        약속잡기_가능_시간_선택을_요청한다(
                location,
                List.of(
                        모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                        모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                        모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
                ), tokenOfMember1
        );
        // 재선택!
        약속잡기_가능_시간_선택을_요청한다(
                location,
                List.of(
                        모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                        모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                        모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
                ), tokenOfMember1
        );
        약속잡기_가능_시간_추천_결과_조회를_요청한다(location, tokenOfMember1);
        약속잡기_마감을_요청한다(location, tokenOfMember1);
        약속잡기_삭제를_요청한다(location, tokenOfMember1);
    }

    private void 투표_API의_성능을_테스트한다() {
        LOG.info("[투표 성능 테스트]");
        투표_목록_조회를_요청한다("/api/groups/code2", tokenOfMember1);
        PollCreateRequest pollCreateRequest = new PollCreateRequest("투표_제목", 2, false, LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2"));
        String pollLocation = 투표_생성을_요청한다("/api/groups/code2", pollCreateRequest, tokenOfMember1).header("Location");
        투표_단건_조회를_요청한다(pollLocation, tokenOfMember1);
        List<PollItemResponse> pollItemResponses = toObjectList(투표_선택항목_조회를_요청한다(pollLocation, tokenOfMember1),
                PollItemResponse.class);
        Long pollItemId1 = pollItemResponses.get(0).getId();
        Long pollItemId2 = pollItemResponses.get(1).getId();
        투표_진행을_요청한다(pollLocation,
                List.of(new PollResultRequest(pollItemId1, "눈물이_나기_때문이에요"), new PollResultRequest(pollItemId2, "그냥녀~")),
                tokenOfMember1);
        // 재투표!
        투표_진행을_요청한다(pollLocation,
                List.of(new PollResultRequest(pollItemId1, "눈물이_나기_때문이에요"), new PollResultRequest(pollItemId2, "그냥녀~")),
                tokenOfMember1);
        투표_마감을_요청한다(pollLocation, tokenOfMember1);
        투표_삭제를_요청한다(pollLocation, tokenOfMember1);
    }
}
