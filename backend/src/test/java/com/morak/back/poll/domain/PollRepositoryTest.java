package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollRepositoryTest {

    @Autowired
    private DomainSupplier supplier;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = factory.createEntityManager();
    }

    @Test
    void 투표를_저장한다() {
        // given
        Team team = supplier.supplyTeam(1L);
        Member member = supplier.supplyMember(1L);
        Poll poll = Poll.builder()
                .team(team)
                .host(member)
                .title("test-title")
                .isAnonymous(false)
                .allowedPollCount(1)
                .status(PollStatus.OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code("testcode")
                .build();

        // when
        Poll savedPoll = pollRepository.save(poll);

        // then
        Assertions.assertAll(
                () -> assertThat(savedPoll).isNotNull(),
                () -> assertThat(savedPoll.getId()).isNotNull(),
                () -> assertThat(savedPoll.getTitle()).isEqualTo("test-title"),
                () -> assertThat(savedPoll.getTeam()).isNotNull(),
                () -> assertThat(savedPoll.getHost()).isNotNull()
        );
    }

    @Test
    void 팀ID로_투표_목록을_조회한다() {
        // given
        long teamId = 1L;

        // when
        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        // then
        assertThat(polls).hasSize(1);
    }

    @Test
    void 투표를_저장할_때_선택항목도_저장한다() {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Poll poll = pollRepository.getById(1L);
        poll.addItem(PollItem.builder()
                .poll(poll)
                .subject("test-subject-1")
                .build());
        poll.addItem(PollItem.builder()
                .poll(poll)
                .subject("test-subject-2")
                .build());
        // when
        entityManager.flush();
        entityManager.clear();

        transaction.commit();

        // then
        Poll findPoll = pollRepository.getById(1L);
        Assertions.assertAll(
                () -> assertThat(findPoll.getPollItems()).hasSize(5),
                () -> assertThat(findPoll.getPollItems().get(3).getSubject()).isEqualTo("test-subject-1")
        );
    }

    @Test
    void 투표_단건을_조회한다() {
        // given
        Poll poll = pollRepository.findByIdAndTeamId(1L, 1L).orElseThrow();

        // when & then
        Assertions.assertAll(
                () -> assertThat(poll.getTitle()).isEqualTo("test-poll-title"),
                () -> assertThat(poll.getHost().getId()).isEqualTo(1L)
        );
    }

    @Test
    void 잘못된_팀_id로_조회할_경우_null을_반환한다() {
        // given
        Optional<Poll> poll = pollRepository.findByIdAndTeamId(1L, 2L);

        // when & then
        assertThat(poll).isEmpty();
    }

    @Test
    void id로_투표를_삭제한다() {
        // given
        pollRepository.deleteById(1L);

        // when
        Optional<Poll> poll = pollRepository.findById(1L);

        // then
        assertThat(poll).isEmpty();
    }
}
