package com.morak.back.appointment.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByTeamId(Long teamId);

    @Query("select a from Appointment a where a.code.code = :code")
    Optional<Appointment> findByCode(String code);

    void deleteById(Long id);
}
