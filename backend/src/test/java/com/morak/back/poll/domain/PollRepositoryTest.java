package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollRepositoryTest {

    @Autowired
    private PollRepository pollRepository;

    // TODO: 2022/08/14 data.sql의존
    @Test
    void 투표를_저장한다() {
        // given
        Team team = Team.builder()
                .id(1L)
                .build();
        Member member = Member.builder()
                .id(1L)
                .build();
        Poll poll = Poll.builder()
                .team(team)
                .host(member)
                .title("test-title")
                .isAnonymous(false)
                .allowedPollCount(1)
                .status(PollStatus.OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code(Code.generate(length -> "unique99"))
                .build();

        // when
        Poll savedPoll = pollRepository.save(poll);

        // then
        assertThat(savedPoll)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(poll);
    }

    // TODO: 2022/08/11 data.sql 의존 제거
    @Test
    void 팀ID로_투표_목록을_조회한다() {
        // given
        long teamId = 1L;

        // when
        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        // then
        assertThat(polls).hasSize(1);
    }

    // TODO: 2022/08/11 data.sql 의존
    @Test
    void 투표_단건을_조회한다() {
        // given
        Poll poll = pollRepository.findByCode("testcode").orElseThrow();

        // when & then
        Assertions.assertAll(
                () -> assertThat(poll.getTitle()).isEqualTo("test-poll-title"),
                () -> assertThat(poll.getHost().getId()).isEqualTo(1L)
        );
    }

    // TODO: 2022/08/11 data.sql 의존
    @Test
    void 잘못된_팀_code로_조회할_경우_null을_반환한다() {
        // given
        Optional<Poll> poll = pollRepository.findByCode("chaleeleeeee");

        // when & then
        assertThat(poll).isEmpty();
    }
}
