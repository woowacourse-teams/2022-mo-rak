package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;

@RepositoryTest
class PollItemRepositoryTest {

    @Autowired
    private DomainSupplier supplier;

    @Autowired
    private PollItemRepository pollItemRepository;

    @DisplayName("투표 선택 항목을 저장한다.")
    @Test
    void savePollItems() {
        // given
        Poll poll = supplier.supplyPoll(1L);

        List<PollItem> items = List.of(
            new PollItem(null, poll, "subject-1"),
            new PollItem(null, poll, "subject-2"),
            new PollItem(null, poll, "subject-3")
        );
        // when
        List<PollItem> savedItems = pollItemRepository.saveAll(items);
        // then
        assertThat(savedItems).allMatch(item -> item.getId() != null);
    }

}