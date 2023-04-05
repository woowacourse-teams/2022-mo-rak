package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends Repository<Poll, Long> {

    Poll save(Poll poll);

    @Query("SELECT p FROM Poll p WHERE p.menu.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    @Query("SELECT p FROM Poll p INNER JOIN Team t ON t.code = p.menu.teamCode WHERE p.menu.status = 'OPEN' AND p.menu.closedAt.closedAt <= :thresholdDateTime")
    List<Poll> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Query("SELECT p FROM Poll p WHERE p.menu.teamCode.code = :teamCode")
    List<Poll> findAllByTeamCode(@Param("teamCode") String teamCode);

    void delete(Poll poll);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Poll p "
            + "set p.pollItems.selectedCount = p.pollItems.selectedCount + 1 "
            + "where p.menu.code.code = :pollCode")
    void updateSelectedCount(@Param("pollCode") String pollCode);
}
