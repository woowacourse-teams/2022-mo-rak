package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
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

    @Test
    void 투표_선택_항목을_저장한다() {
        // given
        Poll poll = supplier.supplyPoll(1L);

        List<PollItem> items = List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("subject-1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("subject-2")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("subject-3")
                        .build()
        );
        // when
        List<PollItem> savedItems = pollItemRepository.saveAll(items);
        // then
        assertThat(savedItems).allMatch(item -> item.getId() != null);
    }

    @Test
    void 투표_선택_항목의_투표_결과_리스트에서_투표_결과를_제거하면_DB에서_삭제된다() {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        PollItem item = pollItemRepository.getById(2L);

        List<PollResult> pollResults = item.getPollResults();

        pollResults.remove(0);

        transaction.commit();

        // when
        PollItem findItem = pollItemRepository.getById(2L);

        // then
        assertThat(findItem.getPollResults()).hasSize(1);
    }

    @Test
    void 투표_id로_투표_선택_항목을_조회한다() {
        // given
        List<PollItem> pollItems = pollItemRepository.findAllByPollId(1L);

        // when & then
        Assertions.assertAll(
                () -> assertThat(pollItems).hasSize(3),
                () -> assertThat(pollItems.get(0).getSubject()).isEqualTo("test-poll-item-subject-A")
        );
    }

}
