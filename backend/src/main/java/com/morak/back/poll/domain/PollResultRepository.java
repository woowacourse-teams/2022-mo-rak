package com.morak.back.poll.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface PollResultRepository extends Repository<PollResult, Long> {
    List<PollResult> saveAll(Iterable<PollResult> pollResults);
}
