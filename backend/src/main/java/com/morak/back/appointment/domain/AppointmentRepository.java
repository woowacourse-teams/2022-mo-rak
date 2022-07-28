package com.morak.back.appointment.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AppointmentRepository extends Repository<Appointment, Long> {

    Appointment save(Appointment appointment);

    Optional<Appointment> findByCode(String code);

    List<Appointment> findAllByTeamId(Long teamId);

    void deleteById(Long id);
}
