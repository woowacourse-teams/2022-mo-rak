package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface AvailableTimeRepository extends Repository<AvailableTime, Long> {

    AvailableTime save(AvailableTime availableTime);

    List<AvailableTime> saveAll(Iterable<AvailableTime> availableTimes);

    List<AvailableTime> findAllByMemberAndAppointment(Member member, Appointment appointment); // 사용하지 않음

    void deleteAllByMemberAndAppointment(Member member, Appointment appointment);

    List<AvailableTime> findAllByAppointment(Appointment appointment);

    void flush();

    void deleteAllByAppointment(Appointment appointment);
}
