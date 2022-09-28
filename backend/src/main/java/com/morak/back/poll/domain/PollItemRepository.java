package com.morak.back.poll.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface PollItemRepository extends Repository<PollItem, Long> {
    List<PollItem> findAllByPoll(Poll poll);

    List<PollItem> saveAll(Iterable<PollItem> items);

    Optional<PollItem> findById(Long id);
}
