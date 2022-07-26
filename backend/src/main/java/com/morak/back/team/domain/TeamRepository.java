package com.morak.back.team.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByCode(String code);

    @Query("select t.id from Team t where t.code = :code")
    Optional<Long> findIdByCode(@Param("code") String code);
}
