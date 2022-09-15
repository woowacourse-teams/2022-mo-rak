package com.morak.back.performance;

import static com.morak.back.AuthSupporter.toHeader;
import static com.morak.back.SimpleRestAssured.delete;
import static com.morak.back.SimpleRestAssured.get;
import static com.morak.back.SimpleRestAssured.patch;
import static com.morak.back.SimpleRestAssured.put;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시반부터_5시_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_4시부터_4시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.모락_회식_첫째날_5시부터_5시반_선택_요청_데이터;
import static com.morak.back.appointment.AppointmentCreateRequestFixture.범위_16_20_약속잡기_요청_데이터;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.extractTeamCodeFromLocation;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_멤버_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_목록_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가_여부_조회를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_참가를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_초대코드_생성을_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.그룹_탈퇴를_요청한다;
import static com.morak.back.team.acceptance.TeamAcceptanceTest.기본_그룹_조회를_요청한다;

import com.morak.back.SimpleRestAssured;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"})
public class PerformanceTest {

    private static final String APPOINTMENT_BASE_PATH = "/api/groups/code1/appointments";
    private static final Logger LOG = LoggerFactory.getLogger("PERFORMANCE");

    private final RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    private String token;
    private String otherToken;
    private Member member;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        token = tokenProvider.createToken(String.valueOf(1L));
        otherToken = tokenProvider.createToken(String.valueOf(2L));
        member = Member.builder().id(1L).build();

        // 500개의 멤버를 저장한다.
        List<Member> members = makeDummyMembers(500);
        batchInsertMembers(members);
        // 1000개의 팀을 저장한다.
        List<Team> teams = makeDummyTeams(1000);
        batchInsertTeams(teams);
        // 멤버1을 모든 팀에 속하게 한다.
        batchInsertTeamMembersMain(1000);
        // 팀1에 9개의 멤버를 속하게 한다.
        List<TeamMember> teamMembers1 = new ArrayList<>();
        for (long i = 2; i <= 10; i++) {
            teamMembers1.add(TeamMember.builder()
                    .team(Team.builder().id(1L).build())
                    .member(Member.builder().id(i).build())
                    .build());
        }
        batchInsertTeamMember(teamMembers1);

        // 멤버 당 네개의 팀에 속하게 한다. (어떤 팀에 들어갈지는 랜덤이다!)
        List<TeamMember> teamMembers = makeDummyTeamMembers();
        batchInsertTeamMember(teamMembers);
        // 팀 당 30개의 약속잡기를 저장한다.
        List<Appointment> appointments = makeDummyAppointments(1000, 30);
        batchInsertAppointment(appointments);
        // 약속잡기 당 00개의 가능 시간을 저장한다.
        List<AvailableTime> availableTimes = makeDummyAvailableTime(1000, 30);
        batchInsertAvailableTime(availableTimes);
    }

    @Test
    void 성능을_테스트한다() {
        LOG.info("[팀 & 멤버 성능 테스트]");
        TeamCreateRequest request = new TeamCreateRequest("모락팀");
        String teamLocation = 그룹_생성을_요청한다(request, token).header("Location");
        String teamInvitationLocation = 그룹_초대코드_생성을_요청한다(teamLocation, token).header("Location");
        그룹_참가_여부_조회를_요청한다(teamInvitationLocation, token);
        그룹_참가를_요청한다(teamInvitationLocation, otherToken);
        그룹_목록_조회를_요청한다(token);
        그룹_멤버_목록_조회를_요청한다(token, teamLocation);
        기본_그룹_조회를_요청한다(otherToken);
        String teamCode = extractTeamCodeFromLocation(teamLocation);
        그룹_탈퇴를_요청한다(teamCode, otherToken);

        LOG.info("[약속잡기 성능 테스트]");
        String location = 약속잡기_생성을_요청한다(범위_16_20_약속잡기_요청_데이터).header("Location");
        System.out.println("location = " + location);
        약속잡기_목록_조회를_요청한다();
        약속잡기_단건_조회를_요청한다(location);
        약속잡기_가능_시간_선택을_요청한다(
                location,
                List.of(
                        모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                        모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                        모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
                )
        );
        // 재선택!
        약속잡기_가능_시간_선택을_요청한다(
                location,
                List.of(
                        모락_회식_첫째날_4시부터_4시반_선택_요청_데이터,
                        모락_회식_첫째날_4시반부터_5시_선택_요청_데이터,
                        모락_회식_첫째날_5시부터_5시반_선택_요청_데이터
                )
        );
        약속잡기_가능_시간_추천_결과_조회를_요청한다(location);
        약속잡기_마감을_요청한다(location);
        약속잡기_삭제를_요청한다(location);
    }

    private List<Member> makeDummyMembers(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> Member.builder()
                        .oauthId(randomCodeGenerator.generate(8))
                        .name("더미 멤버" + (i + 1))
                        .profileUrl("http://" + "테스트 멤버" + "-profile.com")
                        .build())
                .collect(Collectors.toList());
    }

    private List<Team> makeDummyTeams(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> Team.builder()
                        .name("더미 팀" + (i + 1))
                        .code(Code.generate((l) -> "code" + (i + 1)))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Appointment> makeDummyAppointments(int teamSize, int appointmentSize) {
        List<Appointment> appointments = new ArrayList<>();
        for (long i = 1; i <= teamSize; i++) {
            for (int j = 1; j <= appointmentSize; j++) {
                appointments.add(
                        Appointment.builder()
                                .team(Team.builder().id(i).build())
                                .host(member)
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
                                        .member(member)
                                        .startDateTime(
                                                LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 0)))
                                        .endDateTime(
                                                LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 30)))
                                        .build()
                        );
                        availableTimes.add(
                                AvailableTime.builder()
                                        .appointment(appointment)
                                        .member(member)
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

    private void batchInsertMembers(List<Member> members) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) VALUES (?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, members.get(i).getOauthId());
                        ps.setString(2, members.get(i).getName());
                        ps.setString(3, members.get(i).getProfileUrl());
                    }

                    @Override
                    public int getBatchSize() {
                        return members.size();
                    }
                }
        );
    }

    private void batchInsertTeams(List<Team> teams) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team (name, code, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, teams.get(i).getName());
                        ps.setString(2, teams.get(i).getCode());
                    }

                    @Override
                    public int getBatchSize() {
                        return teams.size();
                    }
                }
        );
    }

    private void batchInsertTeamMembersMain(int size) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_member (team_id, member_id, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, i + 1);
                        ps.setLong(2, 1);
                    }

                    @Override
                    public int getBatchSize() {
                        return size;
                    }
                }
        );
    }

    private void batchInsertTeamMember(List<TeamMember> teamMembers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_member (team_id, member_id, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, teamMembers.get(i).getTeam().getId());
                        ps.setLong(2, teamMembers.get(i).getMember().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return teamMembers.size();
                    }
                }
        );
    }

    private void batchInsertAppointment(List<Appointment> appointments) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO appointment (team_id, host_id, title, description, start_date, end_date, start_time, end_time, duration_minutes, status, code, closed_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, appointments.get(i).getTeam().getId());
                        ps.setLong(2, appointments.get(i).getHost().getId());
                        ps.setString(3, appointments.get(i).getTitle());
                        ps.setString(4, appointments.get(i).getDescription());
                        ps.setObject(5, appointments.get(i).getStartDate());
                        ps.setObject(6, appointments.get(i).getEndDate());
                        ps.setObject(7, appointments.get(i).getStartTime());
                        ps.setObject(8, appointments.get(i).getEndTime());
                        ps.setInt(9, appointments.get(i).getDurationMinutes().getDurationMinutes());
                        ps.setString(10, appointments.get(i).getStatus().name());
                        ps.setString(11, appointments.get(i).getCode());
                        ps.setObject(12, appointments.get(i).getClosedAt());
                    }

                    @Override
                    public int getBatchSize() {
                        return appointments.size();
                    }
                }
        );
    }

    private void batchInsertAvailableTime(List<AvailableTime> availableTimes) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, end_date_time, created_at, updated_at) VALUES (?, ?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, availableTimes.get(i).getAppointment().getId());
                        ps.setLong(2, availableTimes.get(i).getMember().getId());
                        ps.setObject(3, availableTimes.get(i).getDateTimePeriod().getStartDateTime());
                        ps.setObject(4, availableTimes.get(i).getDateTimePeriod().getEndDateTime());
                    }

                    @Override
                    public int getBatchSize() {
                        return availableTimes.size();
                    }
                }
        );
    }

    private ExtractableResponse<Response> 약속잡기_생성을_요청한다(AppointmentCreateRequest request) {
        return SimpleRestAssured.post(APPOINTMENT_BASE_PATH, request,
                toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_목록_조회를_요청한다() {
        return SimpleRestAssured.get(APPOINTMENT_BASE_PATH, toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_단건_조회를_요청한다(String location) {
        return SimpleRestAssured.get(location, toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_가능_시간_선택을_요청한다(String location, List<AvailableTimeRequest> requests) {
        return put(location, requests, toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_가능_시간_추천_결과_조회를_요청한다(String location) {
        return get(location + "/recommendation", toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_마감을_요청한다(String location) {
        return patch(location + "/close", toHeader(token));
    }

    private ExtractableResponse<Response> 약속잡기_삭제를_요청한다(String location) {
        return delete(location, toHeader(token));
    }
}
