package com.morak.back.appointment.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface AvailableTimeRepository extends Repository<AvailableTime, Long> {

    AvailableTime save(AvailableTime availableTime);

    List<AvailableTime> saveAll(Iterable<AvailableTime> availableTimes);

    List<AvailableTime> findAllByMemberIdAndAppointmentId(Long memberId, Long appointmentId);

    void deleteAllByMemberIdAndAppointmentId(Long memberId, Long appointmentId);

    List<AvailableTime> findAllByAppointmentId(Long appointmentId);

    void flush();
}
