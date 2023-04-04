package com.morak.back.poll.domain;

import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_00;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_30;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollRepositoryTest {

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
        Poll foundPoll = pollRepository.findByCode(poll.getCode()).orElseThrow();

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
                        .code(Code.generate(l -> "12345678"))
                        .title("모락 회식 메뉴")
                        .teamCode(Code.generate((s) -> team.getCode()))
                        .hostId(member.getId())
                        .status(MenuStatus.OPEN)
                        .closedAt(new ClosedAt(TIME_OF_2022_05_12_12_30, TIME_OF_2022_05_12_12_00))
                        .pollItems(List.of(
                                PollItem.builder().subject("삼겹살").build(),
                                PollItem.builder().subject("회").build(),
                                PollItem.builder().subject("이자카야").build()
                        ))
                        .allowedCount(2)
                        .anonymous(false)
                        .build()
        );
    }

    @Test
    void 선택_인원을_추가한다() {
        // when
        pollRepository.updateSelectedCount(poll.getCode());

        // then
        Optional<Poll> updatedPoll = pollRepository.findByCode(poll.getCode());
        Assertions.assertAll(
                () -> assertThat(updatedPoll).isPresent(),
                () -> assertThat(updatedPoll.get().getSelectedCount()).isOne()
        );
    }
}
