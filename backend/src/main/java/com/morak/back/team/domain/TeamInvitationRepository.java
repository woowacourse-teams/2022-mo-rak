package com.morak.back.team.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TeamInvitationRepository extends Repository<TeamInvitation, Long> {
    @Query("select ti from TeamInvitation ti where ti.code.code = :code")
    Optional<TeamInvitation> findByCode(@Param("code") String code);

    TeamInvitation save(TeamInvitation teamInvitation);
}
