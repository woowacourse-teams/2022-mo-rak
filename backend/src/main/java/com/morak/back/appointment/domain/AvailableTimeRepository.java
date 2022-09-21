package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AvailableTimeRepository extends Repository<AvailableTime, Long> {

    AvailableTime save(AvailableTime availableTime);

    List<AvailableTime> saveAll(Iterable<AvailableTime> availableTimes);

    List<AvailableTime> findAllByMemberAndAppointment(Member member, Appointment appointment); // 사용하지 않음

    @Modifying
    @Query("DELETE FROM AvailableTime at WHERE at.member = :member and at.appointment = :appointment")
    void deleteAllByMemberAndAppointment(@Param("member") Member member, @Param("appointment") Appointment appointment);

    List<AvailableTime> findAllByAppointment(Appointment appointment);

    @Modifying
    @Query("DELETE FROM AvailableTime at WHERE at.appointment = :appointment")
    void deleteAllByAppointment(@Param("appointment") Appointment appointment);
}
