package com.morak.back.role.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // -- A

    // -- B

    // -- C


    // -- D
    Optional<Role> findByTeamCode(String teamCode);

}
