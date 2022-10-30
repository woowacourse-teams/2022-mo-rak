package com.morak.back.performance.support;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.performance.dao.AppointmentDao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("performance")
public class AppointmentDummySupport {

    @Autowired
    private AppointmentDao appointmentDao;

    public void 약속잡기_더미데이터를_추가한다(int teamSize, int appointmentSizePerTeam) {
        List<Appointment> appointments = makeDummyAppointments(teamSize, appointmentSizePerTeam);
        appointmentDao.batchInsertAppointment(appointments);
    }

    public void 약속잡기_선택가능시간_더미데이터를_추가한다(int appointmentSize) {
        List<AvailableTime> availableTimes = makeDummyAvailableTime(appointmentSize);
        appointmentDao.batchInsertAvailableTime(availableTimes);
    }

    public List<Appointment> makeDummyAppointments(int teamSize, int appointmentSizePerTeam) {
        return Stream.iterate(1L, i -> i <= teamSize, i -> i + 1)
                .flatMap(teamIndex -> IntStream.rangeClosed(1, appointmentSizePerTeam)
                        .mapToObj(appointmentIndex -> Appointment.builder()
//                                .team(Team.builder().id(teamIndex).build())
//                                .host(MEMBER_ID1)
                                .title("더미 약속잡기" + appointmentIndex)
                                .subTitle("더미 약속잡기 설명")
                                .startDate(LocalDate.now().plusDays(1))
                                .endDate(LocalDate.now().plusDays(8))
                                .startTime(LocalTime.of(16, 0))
                                .endTime(LocalTime.of(20, 0))
                                .durationHours(1)
                                .durationMinutes(0)
                                .code(Code.generate(new RandomCodeGenerator()))
                                .closedAt(LocalDateTime.now().plusDays(1))
                                .build())
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList());
    }

    public List<AvailableTime> makeDummyAvailableTime(int appointmentSize) {
        List<AvailableTime> availableTimes = new ArrayList<>();
        for (long appointmentIndex = 1; appointmentIndex <= appointmentSize; appointmentIndex++) {
            Appointment appointment = Appointment.builder()
                    .id(appointmentIndex)
                    .startDate(LocalDate.now().plusDays(1))
                    .endDate(LocalDate.now().plusDays(8))
                    .startTime(LocalTime.of(16, 0))
                    .endTime(LocalTime.of(20, 0))
                    .durationHours(1)
                    .durationMinutes(0)
                    .closedAt(LocalDateTime.now().plusDays(1))
                    .build();
            for (int day = 1; day <= 8; day++) {
                for (int hour = 16; hour <= 19; hour++) {
                    availableTimes.add(
                            AvailableTime.builder()
//                                    .member(MEMBER_ID1)
                                    .startDateTime(
                                            LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.of(hour, 0)))
                                    .build()
                    );
                }
            }
        }
        return availableTimes;
    }
}
