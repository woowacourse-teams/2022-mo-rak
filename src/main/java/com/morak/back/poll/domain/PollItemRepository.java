package com.morak.back.poll.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollItemRepository extends JpaRepository<PollItem, Long> {
}
