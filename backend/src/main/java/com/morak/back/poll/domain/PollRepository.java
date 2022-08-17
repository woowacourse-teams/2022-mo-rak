package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PollRepository extends Repository<Poll, Long> {

    Poll save(Poll poll);

    List<Poll> findAllByTeamId(Long teamId);

    @Query("select p from Poll p join fetch p.team where p.status = 'OPEN' and p.closedAt <= :thresholdDateTime")
    List<Poll> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Query("select p from Poll p where p.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("update Poll p set p.status = 'CLOSED' where p.id = :id")
    void closeById(@Param("id") Long id);
}
