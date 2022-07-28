package com.morak.back.team.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends Repository<Team, Long> {

    @Query("select t from Team t where t.code.code = :code")
    Optional<Team> findByCode(String code);

    @Query("select t.id from Team t where t.code.code = :code")
    Optional<Long> findIdByCode(@Param("code") String code);

    Team save(Team team);
}
