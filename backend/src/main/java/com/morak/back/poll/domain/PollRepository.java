package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends Repository<Poll, Long> {

    Poll save(Poll poll);

    List<Poll> findAllByTeamId(Long teamId);

    @Query("select p from Poll p where p.status = 'OPEN' and p.closedAt between :startDateTime and :endDateTime")
    List<Poll> findAllToBeClosed(@Param("startDateTime") LocalDateTime startDateTime,
                                 @Param("endDateTime") LocalDateTime endDateTime);

    @Query("select p from Poll p where p.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    void deleteById(Long id);
}
