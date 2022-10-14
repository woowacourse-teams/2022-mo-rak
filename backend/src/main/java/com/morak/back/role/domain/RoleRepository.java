package com.morak.back.role.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // -- A
    Optional<Role> findByTeamCode(String teamCode);



    // -- B



    // -- C



    // -- D


}
