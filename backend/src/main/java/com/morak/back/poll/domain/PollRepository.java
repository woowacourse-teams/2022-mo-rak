package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @Query("select p from Poll p where p.menu.code.code = :code")
    Optional<Poll> findByCode(@Param("code") String code);

    @Query("select p "
            + "from Poll p "
            + "join fetch p.pollItems.values pi "
            + "left join pi.selectMembers "
            + "where p.menu.code.code = :code")
    Optional<Poll> findFetchedByCode(@Param("code") String pollCode);

    @Query("select p from Poll p inner join Team t on t.code = p.menu.teamCode where p.menu.status = 'OPEN' and p.menu.closedAt.closedAt <= :thresholdDateTime")
    List<Poll> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Query("select p from Poll p where p.menu.teamCode.code = :teamCode")
    List<Poll> findAllByTeamCode(@Param("teamCode") String teamCode);
}
