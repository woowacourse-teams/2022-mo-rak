package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;

@SpringBootTest
class PollItemRepositoryTest {

    @Autowired
    private DomainSupplier supplier;

    @Autowired
    private PollItemRepository pollItemRepository;

    @Autowired
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = factory.createEntityManager();
    }

    @DisplayName("투표 선택 항목을 저장한다.")
    @Test
    void savePollItems() {
        // given
        Poll poll = supplier.supplyPoll(1L);

        List<PollItem> items = List.of(
            new PollItem(null, poll, "subject-1", new ArrayList<>()),
            new PollItem(null, poll, "subject-2", new ArrayList<>()),
            new PollItem(null, poll, "subject-3", new ArrayList<>())
        );
        // when
        List<PollItem> savedItems = pollItemRepository.saveAll(items);
        // then
        assertThat(savedItems).allMatch(item -> item.getId() != null);
    }

    @Autowired
    private PollResultRepository pollResultRepository;

    @DisplayName("투표 결과를 리스트에서 제거하면 DB에서 삭제된다.")
    @Test
    @Transactional
    void temp() {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        PollItem item = pollItemRepository.getById(2L);

        List<PollResult> pollResults = item.getPollResults();

        pollResults.remove(0);


        List<PollResult> ttt = entityManager.createQuery("select p from PollResult p",
                PollResult.class)
            .getResultList();

        System.out.println("ttt.size() = " + ttt.size());

        transaction.commit();

        // when
        PollItem findItem = pollItemRepository.getById(2L);
        // then
        assertThat(findItem.getPollResults()).hasSize(1);
    }

}