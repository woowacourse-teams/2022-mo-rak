package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends Repository<Poll, Long> {

    Poll save(Poll poll);

    @Query("SELECT p FROM Poll p WHERE p.menu.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    @Query("SELECT p FROM Poll p WHERE p.menu.status = 'OPEN' and p.menu.closedAt.closedAt between :startThresholdDateTime and :endThresholdDateTime")
    List<Poll> findAllToBeClosed(@Param("startThresholdDateTime") LocalDateTime startThresholdDateTime,
                                 @Param("endThresholdDateTime") LocalDateTime endThresholdDateTime);

    @Query("SELECT p FROM Poll p WHERE p.menu.teamCode.code = :teamCode")
    List<Poll> findAllByTeamCode(@Param("teamCode") String teamCode);

    void delete(Poll poll);
}
