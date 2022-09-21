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

    List<Appointment> findAllByTeam(Team team);

    @Query("select a from Appointment a where a.code.code = :code")
    Optional<Appointment> findByCode(@Param("code") String code);

    void delete(Appointment appointment);

    @Query("select a from Appointment a join fetch a.team where a.status = 'OPEN' and a.closedAt <= :thresholdDateTime")
    List<Appointment> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Modifying
    @Query("update Appointment a set a.status = 'CLOSED' where a.id in :ids")
    void closeAllByIds(@Param("ids") Iterable<Long> ids);
}
