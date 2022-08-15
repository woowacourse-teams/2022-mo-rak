package com.morak.back.appointment.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByTeamId(Long teamId);

    @Query("select a from Appointment a where a.code.code = :code")
    Optional<Appointment> findByCode(String code);

    void deleteById(Long id);

    @Query("select a from Appointment a where a.status = 'OPEN' and a.closedAt between :startDateTime and :endDateTime")
    List<Appointment> findAllToBeClosed(@Param("startDateTime") LocalDateTime startDateTime,
                                        @Param("endDateTime") LocalDateTime endDateTime);
}
