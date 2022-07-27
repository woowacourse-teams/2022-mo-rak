package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollResultRepositoryTest {

    @Autowired
    private PollResultRepository pollResultRepository;

    @Autowired
    private DomainSupplier supplier;

    @Test
    void 투표_결과를_저장한다() {
        // given
        Member member = supplier.supplyMember(1L);
        PollItem pollItemA = supplier.supplyPollItem(1L);
        PollItem pollItemB = supplier.supplyPollItem(2L);

        // when
        List<PollResult> pollResults = List.of(
                PollResult.builder()
                        .pollItem(pollItemA)
                        .member(member)
                        .description("에덴이_시킴")
                        .build(),
                PollResult.builder()
                        .pollItem(pollItemB)
                        .member(member)
                        .description("엘리가_시킴")
                        .build()
        );
        List<PollResult> savedPollResults = pollResultRepository.saveAll(pollResults);

        // then
        assertThat(savedPollResults).allMatch(pollResult -> pollResult.getId() != null);
    }
}
