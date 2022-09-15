package com.morak.back.performance;

import static com.morak.back.SimpleRestAssured.toObjectList;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_가능_시간_선택을_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_가능_시간_추천_결과_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_단건_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_마감을_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_목록_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_삭제를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.약속잡기_생성을_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_단건_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_마감을_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_목록_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_삭제를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_생성을_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_선택항목_조회를_요청한다;
import static com.morak.back.performance.PerformanceTestRequestFixture.투표_진행을_요청한다;
import static com.morak.back.poll.domain.PollStatus.OPEN;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.extractTeamCodeFromLocation;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_멤버_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가_여부_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_초대코드_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_탈퇴를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.기본_그룹_조회를_요청한다;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.performance.dao.AppointmentDao;
import com.morak.back.performance.dao.PollDao;
import com.morak.back.performance.dao.TeamMemberDao;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollResult;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    private final RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TeamMemberDao teamMemberDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private PollDao pollDao;

    @LocalServerPort
    int port;

    private Member member1;
    private Member member2;
    private String tokenOfMember1;
    private String tokenOfMember2;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        member1 = Member.builder().id(1L).build();
        member2 = Member.builder().id(2L).build();

        tokenOfMember1 = tokenProvider.createToken(String.valueOf(1L));
        tokenOfMember2 = tokenProvider.createToken(String.valueOf(2L));

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
        int memberSize = 500;
        int teamSize = 1000;
        int appointmentSizePerTeam = 30;
        int pollSizePerTeam = 30;

        멤버_더미데이터를_추가한다(memberSize);
        팀_더미데이터를_추가한다(teamSize);
        팀_멤버_더미데이터를_추가한다(teamSize);

        약속잡기_더미데이터를_추가한다(teamSize, appointmentSizePerTeam);
        약속잡기_선택가능시간_더미데이터를_추가한다(teamSize, appointmentSizePerTeam);

        투표_더미데이터를_추가한다(teamSize, pollSizePerTeam);
        투표_선택항목_더미데이터를_추가한다();
        투표_선택결과_더미데이터를_추가한다();
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

    private void 멤버_더미데이터를_추가한다(int memberSize) {
        List<Member> members = makeDummyMembers(memberSize);
        teamMemberDao.batchInsertMembers(members);
    }

    private void 팀_더미데이터를_추가한다(int teamSize) {
        List<Team> teams = makeDummyTeams(teamSize);
        teamMemberDao.batchInsertTeams(teams);
    }

    private void 팀_멤버_더미데이터를_추가한다(int teamSize) {
        // 멤버1을 모든 팀에 속하게 한다.
        teamMemberDao.batchInsertTeamMembersMain(teamSize);
        // 팀1에 9개의 멤버를 속하게 한다.
        List<TeamMember> teamMembers1 = new ArrayList<>();
        for (long i = 2; i <= 10; i++) {
            teamMembers1.add(TeamMember.builder()
                    .team(Team.builder().id(1L).build())
                    .member(Member.builder().id(i).build())
                    .build());
        }
        teamMemberDao.batchInsertTeamMember(teamMembers1);
        // 멤버 당 네개의 팀에 속하게 한다. (어떤 팀에 들어갈지는 랜덤이다!)
        List<TeamMember> teamMembers = makeDummyTeamMembers();
        teamMemberDao.batchInsertTeamMember(teamMembers);
    }

    private void 약속잡기_더미데이터를_추가한다(int teamSize, int appointmentSizePerTeam) {
        List<Appointment> appointments = makeDummyAppointments(teamSize, appointmentSizePerTeam);
        appointmentDao.batchInsertAppointment(appointments);
    }

    private void 약속잡기_선택가능시간_더미데이터를_추가한다(int teamSize, int appointmentSizePerTeam) {
        List<AvailableTime> availableTimes = makeDummyAvailableTime(teamSize, appointmentSizePerTeam);
        appointmentDao.batchInsertAvailableTime(availableTimes);
    }

    private void 투표_더미데이터를_추가한다(int teamSize, int pollSizePerTeam) {
        List<Poll> polls = makeDummyPolls(teamSize, pollSizePerTeam);
        pollDao.batchInsertPolls(polls);
    }

    private void 투표_선택항목_더미데이터를_추가한다() {
        // 투표 당 3개의 선택 항목을 저장한다.
        List<PollItem> pollItems = makeDummyPollItems();
        pollDao.batchInsertPollItems(pollItems);
    }

    private void 투표_선택결과_더미데이터를_추가한다() {
        // 멤버1이 모든 선택 항목을 선택한다.
        List<PollResult> pollResults1 = makeDummyPollResult(member1);
        // 멤버2가 모든 선택 항목을 선택한다.
        List<PollResult> pollResults2 = makeDummyPollResult(member2);
        pollResults1.addAll(pollResults2);
        pollDao.batchInsertPollResults(pollResults1);
    }

    private List<Member> makeDummyMembers(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(i -> Member.builder()
                        .oauthId(randomCodeGenerator.generate(8))
                        .name("더미 멤버" + i)
                        .profileUrl("http://" + "테스트 멤버" + "-profile.com")
                        .build())
                .collect(Collectors.toList());
    }

    private List<Team> makeDummyTeams(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(i -> Team.builder()
                        .name("더미 팀" + i)
                        .code(Code.generate((l) -> "code" + i))
                        .build())
                .collect(Collectors.toList());
    }

    private List<TeamMember> makeDummyTeamMembers() {
        List<TeamMember> teamMembers = new ArrayList<>();
        for (long i = 1; i <= 500; i++) {
            for (int j = 0; j < 4; j++) {
                teamMembers.add(TeamMember.builder()
                        .team(Team.builder().id(ThreadLocalRandom.current().nextLong(1000) + 1).build())
                        .member(Member.builder().id(i).build())
                        .build());
            }
        }
        return teamMembers;
    }

    private List<Appointment> makeDummyAppointments(int teamSize, int appointmentSize) {
        List<Appointment> appointments = new ArrayList<>();
        for (long i = 1; i <= teamSize; i++) {
            for (int j = 1; j <= appointmentSize; j++) {
                appointments.add(
                        Appointment.builder()
                                .team(Team.builder().id(i).build())
                                .host(member1)
                                .title("더미 약속잡기" + j)
                                .description("더미 약속잡기 설명")
                                .startDate(LocalDate.now().plusDays(1))
                                .endDate(LocalDate.now().plusDays(8))
                                .startTime(LocalTime.of(16, 0))
                                .endTime(LocalTime.of(20, 0))
                                .durationHours(1)
                                .durationMinutes(0)
                                .code(Code.generate(new RandomCodeGenerator()))
                                .closedAt(LocalDateTime.now().plusDays(1))
                                .build()
                );
            }
        }
        return appointments;
    }

    private List<AvailableTime> makeDummyAvailableTime(int teamSize, int appointmentSize) {
        List<AvailableTime> availableTimes = new ArrayList<>();
        for (long i = 1; i <= teamSize; i++) {
            for (long j = 1; j <= appointmentSize; j++) {
                Appointment appointment = Appointment.builder()
                        .id(((i - 1) * appointmentSize) + j)
                        .startDate(LocalDate.now().plusDays(1))
                        .endDate(LocalDate.now().plusDays(8))
                        .startTime(LocalTime.of(16, 0))
                        .endTime(LocalTime.of(20, 0))
                        .durationHours(1)
                        .durationMinutes(0)
                        .closedAt(LocalDateTime.now().plusDays(1))
                        .build();
                for (int day = 1; day <= 8; day++) {
                    for (int hour = 16; hour <= 19; hour++) {
                        availableTimes.add(
                                AvailableTime.builder()
                                        .appointment(appointment)
                                        .member(member1)
                                        .startDateTime(
                                                LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 0)))
                                        .endDateTime(
                                                LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 30)))
                                        .build()
                        );
                        availableTimes.add(
                                AvailableTime.builder()
                                        .appointment(appointment)
                                        .member(member1)
                                        .startDateTime(
                                                LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 30)))
                                        .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(day),
                                                LocalTime.of(hour + 1, 0)))
                                        .build()
                        );
                    }
                }
            }
        }
        return availableTimes;
    }

    private List<Poll> makeDummyPolls(int teamSize, int pollSize) {
        List<Poll> polls = new ArrayList<>();
        for (long i = 1; i <= teamSize; i++) {
            for (int j = 1; j <= pollSize; j++) {
                polls.add(
                        Poll.builder()
                                .team(Team.builder().id(i).build())
                                .host(member1)
                                .title("더미 투표" + j)
                                .allowedPollCount(3)
                                .isAnonymous(false)
                                .status(OPEN)
                                .closedAt(LocalDateTime.now().plusDays(1L))
                                .code(Code.generate(new RandomCodeGenerator()))
                                .build()
                );
            }
        }
        return polls;
    }

    private List<PollItem> makeDummyPollItems() {
        List<PollItem> pollItems = new ArrayList<>();
        for (long i = 1; i <= 30_000; i++) {
            for (int j = 1; j <= 3; j++) {
                pollItems.add(
                        PollItem.builder()
                                .poll(Poll.builder().id(i).build())
                                .subject("더미 투표 선택 항목" + j)
                                .build()
                );
            }
        }
        return pollItems;
    }

    private List<PollResult> makeDummyPollResult(Member member) {
        List<PollResult> pollResults = new ArrayList<>();
        for (long i = 1; i <= 90_000; i++) {
            pollResults.add(
                    PollResult.builder()
                            .pollItem(PollItem.builder().id(i).build())
                            .member(member)
                            .description("더미 투표 선택 결과")
                            .build()
            );
        }
        return pollResults;
    }
}
