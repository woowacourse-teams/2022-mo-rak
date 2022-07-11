package com.morak.back.poll.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByTeamId(Long teamId);

    Optional<Poll> findByIdAndTeamId(Long id, Long teamId);
}
