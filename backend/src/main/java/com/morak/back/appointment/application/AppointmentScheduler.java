package com.morak.back.appointment.application;

import com.morak.back.core.support.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentScheduler {

    private final AppointmentService appointmentService;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Generated
    void scheduleAppointment() {
        appointmentService.notifyClosedBySchedule();
    }
}
