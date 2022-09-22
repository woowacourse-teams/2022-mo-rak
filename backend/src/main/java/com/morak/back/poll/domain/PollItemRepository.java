package com.morak.back.poll.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PollItemRepository extends Repository<PollItem, Long> {
    List<PollItem> findAllByPoll(Poll poll);

    List<PollItem> saveAll(Iterable<PollItem> items);

    Optional<PollItem> findById(Long id);

    @Query("SELECT pi FROM PollItem pi WHERE pi.id in :ids")
    List<PollItem> findAllByIds(@Param("ids") List<Long> ids);
}
