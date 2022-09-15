package com.morak.back.appointment.domain.appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByTeamId(Long teamId);

    @Query("select a from Appointment a where a.code.code = :code")
    Optional<Appointment> findByCode(@Param("code") String code);

    void deleteById(Long id);

    @Query("select a from Appointment a join fetch a.team where a.status = 'OPEN' and a.closedAt.closedAt <= :thresholdDateTime")
    List<Appointment> findAllToBeClosed(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);

    @Transactional
    @Modifying
    @Query("update Appointment a set a.status = 'CLOSED' where a.id = :id")
    void closeById(@Param("id") Long id);
}
