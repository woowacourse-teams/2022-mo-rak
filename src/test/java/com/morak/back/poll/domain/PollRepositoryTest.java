package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @DisplayName("팀ID와 호스트ID로 투표 목록을 조회한다.")
    @Test
    void findPollsByTeamIdAndHostId() {
        // given

        long teamId = 1L;
        long memberId = 1L;
        // when
        List<Poll> polls = pollRepository.findAllByTeamIdAndHostId(teamId, memberId);

        // then
        assertThat(polls).hasSize(1);
    }

    @DisplayName("투표를 저장할 때 선택항목도 저장한다.")
    @Test
    void temp() {
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
}