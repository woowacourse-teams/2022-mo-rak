package com.morak.back.brandnew.repository;

import com.morak.back.brandnew.domain.NewPoll;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewPollRepository extends JpaRepository<NewPoll, Long> {

    @Query("select p from NewPoll p where p.pollInfo.code.code = :code")
    Optional<NewPoll> findByCode(@Param("code") String code);

    @Query("select p "
            + "from NewPoll p "
            + "join fetch p.pollItems.values pi "
            + "left join pi.selectMembers.values "
            + "where p.pollInfo.code.code = :code")
    Optional<NewPoll> findFetchedByCode(@Param("code") String pollCode);
}
