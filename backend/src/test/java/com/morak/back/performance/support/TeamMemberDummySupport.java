package com.morak.back.performance.support;

import static com.morak.back.performance.Fixture.MEMBER_ID1;
import static com.morak.back.performance.Fixture.MEMBER_ID2;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.performance.dao.TeamMemberDao;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class TeamMemberDummySupport {

    @Autowired
    private TeamMemberDao teamMemberDao;

    public void 멤버_더미데이터를_추가한다(int memberSize) {
        List<Member> members = makeDummyMembers(memberSize);
        teamMemberDao.batchInsertMembers(members);
    }

    public void 팀_더미데이터를_추가한다(int teamSize) {
        List<Team> teams = makeDummyTeams(teamSize);
        teamMemberDao.batchInsertTeams(teams);
    }

    public void 팀_멤버_더미데이터를_추가한다(int memberSize, int teamSize, int joinSize) {
        // 멤버1을 모든 팀에 속하게 한다.
        List<TeamMember> teamMembers = makeDummyTeamMembersJoinTeams(MEMBER_ID1, teamSize);
        // 멤버2를 팀1~5개 팀에 속하게 한다.
        teamMembers.addAll(makeDummyTeamMembersJoinTeams(MEMBER_ID2, 5));
        // 멤버 당 4개의 팀에 속하게 한다. (어떤 팀에 들어갈지는 랜덤)
        teamMembers.addAll(makeDummyTeamMembers(memberSize, teamSize, joinSize));
        teamMemberDao.batchInsertTeamMember(teamMembers);
    }

    public List<Member> makeDummyMembers(int memberSize) {
        return IntStream.rangeClosed(1, memberSize)
                .mapToObj(memberIndex -> Member.builder()
                        .oauthId("oauth-id" + memberIndex)
                        .name("더미 멤버" + memberIndex)
                        .profileUrl("http://" + "더미 멤버" + memberIndex + "-profile.com")
                        .build())
                .collect(Collectors.toList());
    }

    public List<Team> makeDummyTeams(int teamSize) {
        return IntStream.rangeClosed(1, teamSize)
                .mapToObj(teamIndex -> Team.builder()
                        .name("더미 팀" + teamIndex)
                        .code(Code.generate((l) -> "code" + teamIndex))
                        .build())
                .collect(Collectors.toList());
    }

    public List<TeamMember> makeDummyTeamMembers(int memberSize, int teamSize, int joinSize) {
        return Stream.iterate(3L, i -> i <= memberSize, i -> i + 1)
                .flatMap(memberIndex -> IntStream.range(0, joinSize)
                        .mapToObj(i -> TeamMember.builder()
                                .team(Team.builder().id(ThreadLocalRandom.current().nextLong(teamSize) + 1).build())
                                .member(Member.builder().id(memberIndex).build())
                                .build())
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList());
    }

    public List<TeamMember> makeDummyTeamMembersJoinTeams(Member member, int teamSize) {
        return LongStream.rangeClosed(1, teamSize)
                .mapToObj(teamIndex -> TeamMember.builder()
                        .team(Team.builder().id(teamIndex).build())
                        .member(member)
                        .build())
                .collect(Collectors.toList());
    }
}
