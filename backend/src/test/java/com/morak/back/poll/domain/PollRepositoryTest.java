package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollRepositoryTest {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollItemRepository pollItemRepository;

    @Autowired
    private PollResultRepository pollResultRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
    void 팀으로_투표_목록을_조회한다() {
        // given
        Team team = Team.builder()
                .id(1L)
                .build();

        // when
        List<Poll> polls = pollRepository.findAllByTeam(team);

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

    @Test
    void 포뮬라를_적용해_count를_불러온다() {
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
                .title("테스트 투표")
                .isAnonymous(false)
                .allowedPollCount(1)
                .status(PollStatus.OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code(Code.generate(length -> "unique99"))
                .build();
        pollRepository.save(poll);

        PollItem 테스트_선택_항목1 = PollItem.builder()
                .poll(poll)
                .subject("테스트 선택 항목1")
                .build();
        PollItem 테스트_선택_항목2 = PollItem.builder()
                .poll(poll)
                .subject("테스트 선택 항목2")
                .build();
        pollItemRepository.saveAll(List.of(테스트_선택_항목1, 테스트_선택_항목2));

        PollResult 테스트_선택_항목1에_대한_이유 = PollResult.builder()
                .pollItem(테스트_선택_항목1)
                .member(member)
                .description("테스트 선택 항목1에 대한 이유")
                .build();
        pollResultRepository.saveAll(List.of(테스트_선택_항목1에_대한_이유));

        entityManager.detach(poll);

        // when
        Poll foundPoll = pollRepository.findByCode(poll.getCode()).orElseThrow();

        // then
        assertThat(foundPoll.getCount()).isEqualTo(1);
    }

    @Test
    void 종료할_투표를_가져온다() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(now);

        // then
        assertThat(pollsToBeClosed).hasSize(1);
    }

    @Test
    void ID목록으로_투표를_종료한다() {
        // given
        Poll poll = pollRepository.findByCode("testcode").orElseThrow();

        // when
        pollRepository.closeAllByIds(List.of(poll.getId()));
        entityManager.detach(poll);

        // then
        assertThat(pollRepository.findByCode("testcode").get().getStatus())
                .isEqualTo(PollStatus.CLOSED);
    }
}
