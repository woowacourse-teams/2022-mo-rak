package com.morak.back.role.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface RoleRepository extends Repository<Role, Long> {

    Role save(Role role);

    Optional<Role> findByTeamCode(String teamCode);
}
