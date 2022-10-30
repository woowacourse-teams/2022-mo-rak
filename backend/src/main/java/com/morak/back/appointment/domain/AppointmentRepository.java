package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    @Query("SELECT a FROM Appointment a WHERE a.menu.teamCode.code = :teamCode")
    List<Appointment> findAllByTeamCode(@Param("teamCode") String teamCode);

    @Query("select a from Appointment a where a.menu.code.code = :code")
    Optional<Appointment> findByCode(@Param("code") String code);

    void delete(Appointment appointment);

    @Query("select a from Appointment a where a.menu.status = 'OPEN' and a.menu.closedAt.closedAt <= :thresholdDateTime")
    List<Appointment> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);
}
