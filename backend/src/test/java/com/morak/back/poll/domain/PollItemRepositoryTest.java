package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollItemRepositoryTest {

    @Autowired
    private PollItemRepository pollItemRepository;

    // TODO: 2022/08/14 data.sql 의존
    @Test
    void 투표_선택_항목을_저장한다() {
        // given
        Poll poll = Poll.builder()
                .id(1L)
                .build();

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
        assertThat(savedItems)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(items);
    }

    // TODO: 2022/08/11 data.sql 의존
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
