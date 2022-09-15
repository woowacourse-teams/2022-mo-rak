package com.morak.back.appointment.domain.availabletime;

import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.auth.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AvailableTimeRepository extends Repository<AvailableTime, Long> {

    List<AvailableTime> saveAll(Iterable<AvailableTime> availableTimes);

    @Modifying
    @Query("DELETE FROM AvailableTime at WHERE at.member = :member and at.appointment = :appointment")
    void deleteAllByMemberAndAppointment(@Param("member") Member member, @Param("appointment") Appointment appointment);

    List<AvailableTime> findAllByAppointment(Appointment appointment);

    void flush();

    @Modifying
    @Query("DELETE FROM AvailableTime at WHERE at.appointment = :appointment")
    void deleteAllByAppointment(@Param("appointment") Appointment appointment);
}
