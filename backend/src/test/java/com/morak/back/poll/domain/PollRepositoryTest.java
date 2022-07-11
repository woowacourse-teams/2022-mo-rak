package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.Team;
import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;

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

    @DisplayName("투표를 저장한다.")
    @Test
    void savePoll() {
        // given
        Team team = supplier.supplyTeam(1L);
        Member member = supplier.supplyMember(1L);
        Poll poll = new Poll(null, team, member, "test-title", 1, false, PollStatus.OPEN, LocalDateTime.now(),
            "test-code");

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

    @DisplayName("팀ID로 투표 목록을 조회한다.")
    @Test
    void findPollsByTeamIdAndHostId() {
        // given

        long teamId = 1L;
        long memberId = 1L;
        // when
        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        // then
        assertThat(polls).hasSize(1);
    }

    @DisplayName("투표를 저장할 때 선택항목도 저장한다.")
    @Test
    void savePollAndPollItems() {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Poll poll = pollRepository.getById(1L);
        poll.addItem(new PollItem(null, poll, "test-subject-1"));
        poll.addItem(new PollItem(null, poll, "test-subject-2"));
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

    @DisplayName("투표 단건을 조회한다.")
    @Test
    void findByIdAndTeamId() {
        // given
        Poll poll = pollRepository.findByIdAndTeamId(1L, 1L).orElseThrow();

        // when & then
        Assertions.assertAll(
            () -> assertThat(poll.getTitle()).isEqualTo("test-poll-title"),
            () -> assertThat(poll.getHost().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("잘못된 팀 id로 조회할 경우 null을 반환한다.")
    @Test
    void validateInvalidTeamId() {
        // given
        Optional<Poll> poll = pollRepository.findByIdAndTeamId(1L, 2L);

        // when & then
        assertThat(poll).isEmpty();
    }

    @DisplayName("id로 투표를 삭제한다.")
    @Test
    public void deleteById() {
        // given
        pollRepository.deleteById(1L);

        // when
        Optional<Poll> poll = pollRepository.findById(1L);

        // then
        assertThat(poll).isEmpty();
    }
}