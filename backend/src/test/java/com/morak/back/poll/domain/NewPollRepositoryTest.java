package com.morak.back.poll.domain;

import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_00;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_30;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class NewPollRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PollRepository pollRepository;

    private Team team;
    private Member member;
    private Poll poll;

    @BeforeEach
    void setUp() {
        team = saveTeam();
        member = saveMember();
        poll = savePoll();
    }

    @Test
    void 코드로_투표를_불러온다() {
        // when
        Poll foundPoll = pollRepository.findByCode(poll.getPollInfo().getCode()).orElseThrow();

        // then
        assertThat(foundPoll).isEqualTo(poll);
    }

    @Test
    void 코드로_투표를_불러온다_fetched() {
        // when
        Poll foundPoll = pollRepository.findFetchedByCode(poll.getPollInfo().getCode()).orElseThrow();

        // then
        assertThat(foundPoll).isEqualTo(poll);
    }

    private Team saveTeam() {
        return teamRepository.save(
                Team.builder()
                        .code(Code.generate(l -> "teamcode"))
                        .name("모락")
                        .build()
        );
    }

    private Member saveMember() {
        return memberRepository.save(
                Member.builder()
                        .oauthId("test-oauth-id")
                        .name("엘리")
                        .profileUrl("http://test-profile.png")
                        .build()
        );
    }

    private Poll savePoll() {
        return pollRepository.save(
                Poll.builder()
                        .pollInfo(PollInfo.builder()
                                .codeGenerator(l -> "12345678")
                                .title("모락 회식 메뉴")
                                .anonymous(false)
                                .allowedCount(2)
                                .teamId(team.getId())
                                .hostId(member.getId())
                                .status(PollStatus.OPEN)
                                .closedAt(SystemDateTime.builder()
                                        .dateTime(TIME_OF_2022_05_12_12_30)
                                        .now(TIME_OF_2022_05_12_12_00)
                                        .build())
                                .build()
                        )
                        .pollItems(List.of(
                                PollItem.builder().subject("삼겹살").build(),
                                PollItem.builder().subject("회").build(),
                                PollItem.builder().subject("이자카야").build()
                        ))
                        .build()
        );
    }
}