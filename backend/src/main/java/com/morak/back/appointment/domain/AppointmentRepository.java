package com.morak.back.appointment.domain;

import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByMenuTeam(Team team);

    @Query("select a from Appointment a where a.menu.code.code = :code")
    Optional<Appointment> findByCode(@Param("code") String code);

    void delete(Appointment appointment);

    @Query("select a from Appointment a join fetch a.menu.team where a.menu.status = 'OPEN' and a.menu.closedAt.closedAt <= :thresholdDateTime")
    List<Appointment> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Modifying
    @Query("update Appointment a set a.menu.status = 'CLOSED' where a.id in :ids")
    void closeAllByIds(@Param("ids") Iterable<Long> ids);
}
