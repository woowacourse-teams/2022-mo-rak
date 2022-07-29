package com.morak.back.poll.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByTeamId(Long teamId);

    @Query("select p from Poll p where p.code.code = :code and p.team.id = :teamId")
    Optional<Poll> findByCodeAndTeamId(String code, Long teamId);
}
