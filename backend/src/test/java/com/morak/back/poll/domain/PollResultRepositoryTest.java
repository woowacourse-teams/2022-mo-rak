package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PollResultRepositoryTest {

    @Autowired
    private PollResultRepository pollResultRepository;

    @Autowired
    private DomainSupplier supplier;

    @DisplayName("투표 결과를 저장한다.")
    @Test
    void savePollResults() {
        // given
        Member member = supplier.supplyMember(1L);
        PollItem pollItemA = supplier.supplyPollItem(1L);
        PollItem pollItemB = supplier.supplyPollItem(2L);
        // when

        // then
        List<PollResult> pollResults = List.of(new PollResult(null, pollItemA, member), new PollResult(null, pollItemB, member));
        List<PollResult> savedPollResults = pollResultRepository.saveAll(pollResults);

        assertThat(savedPollResults).allMatch(pollResult -> pollResult.getId() != null);
    }

}
