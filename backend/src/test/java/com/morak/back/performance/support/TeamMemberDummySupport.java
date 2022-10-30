package com.morak.back.performance.support;

import com.morak.back.performance.dao.TeamMemberDao;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("performance")
public class TeamMemberDummySupport {

    @Autowired
    private TeamMemberDao teamMemberDao;

    public void 멤버_더미데이터를_추가한다(int memberSize) {
        teamMemberDao.batchInsertMembers(memberSize);
    }

    public void 팀_더미데이터를_추가한다(int teamSize) {
        teamMemberDao.batchInsertTeams(teamSize);
    }

    public void 팀_멤버_더미데이터를_추가한다(int memberSize, int teamSize, int joinSize) {
        // 멤버1을 모든 팀에 속하게 한다.
        List<Entry<Long, Long>> teamMembers = makeDummyTeamMembersJoinTeams(1L, teamSize);
        // 멤버2를 팀1~5개 팀에 속하게 한다.
        teamMembers.addAll(makeDummyTeamMembersJoinTeams(2L, 5));
        // 멤버 당 4개의 팀에 속하게 한다. (어떤 팀에 들어갈지는 랜덤)
        teamMembers.addAll(makeDummyTeamMembers(memberSize, teamSize, joinSize));
        teamMemberDao.batchInsertTeamMember(teamMembers);
    }

    private List<Entry<Long, Long>> makeDummyTeamMembers(int memberSize, int teamSize, int joinSize) {
        return Stream.iterate(3L, i -> i <= memberSize, i -> i + 1)
                .flatMap(memberIndex -> IntStream.range(0, joinSize)
                        .mapToObj(i -> Map.entry(ThreadLocalRandom.current().nextLong(teamSize) + 1, memberIndex))
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList());
    }

    private List<Entry<Long, Long>> makeDummyTeamMembersJoinTeams(Long memberId, int teamSize) {
        return LongStream.rangeClosed(1, teamSize)
                .mapToObj(teamIndex -> Map.entry(teamIndex, memberId))
                .collect(Collectors.toList());
    }
}
