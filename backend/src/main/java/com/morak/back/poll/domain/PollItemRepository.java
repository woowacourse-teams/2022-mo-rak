package com.morak.back.poll.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollItemRepository extends JpaRepository<PollItem, Long> {
    List<PollItem> findAllByPollId(Long pollId);
}
