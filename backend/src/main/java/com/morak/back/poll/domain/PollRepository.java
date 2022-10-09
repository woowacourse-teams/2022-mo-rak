package com.morak.back.poll.domain;

import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends Repository<Poll, Long> {

    Poll save(Poll poll);

    List<Poll> findAllByTeam(Team team);

    @Query("select p from Poll p join fetch p.team where p.status = 'OPEN' and p.closedAt <= :thresholdDateTime")
    List<Poll> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Query("select p from Poll p where p.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    @Query("select p from Poll p join fetch p.pollItems where p.code.code = :code")
    Optional<Poll> findFetchedByCode(@Param("code") String code);

    void delete(Poll poll);

    @Modifying
    @Query("update Poll p set p.status = 'CLOSED' where p in :polls")
    void closeAll(@Param("polls") Iterable<Poll> polls);
}
