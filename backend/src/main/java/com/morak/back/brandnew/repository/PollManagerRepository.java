package com.morak.back.brandnew.repository;

import com.morak.back.brandnew.domain.PollManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollManagerRepository extends JpaRepository<PollManager, Long> {

    @Query("select p from PollManager p where p.poll.code.code = :code")
    Optional<PollManager> findByCode(@Param("code") String code);
}
