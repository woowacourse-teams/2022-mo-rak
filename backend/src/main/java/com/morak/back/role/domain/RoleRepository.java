package com.morak.back.role.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends Repository<Role, Long> {

    Role save(Role role);


    @Query("SELECT r FROM Role r WHERE r.teamCode.code = :code")
    Optional<Role> findByTeamCode(@Param("code") String teamCode);

    @Query("SELECT r FROM Role r "
            + "LEFT JOIN FETCH r.roleHistories.values v "
            + "LEFT JOIN FETCH v.matchResults "
            + "WHERE r.teamCode.code = :code")
    Optional<Role> findByTeamCodeFetched(@Param("code") String teamCode);

}
