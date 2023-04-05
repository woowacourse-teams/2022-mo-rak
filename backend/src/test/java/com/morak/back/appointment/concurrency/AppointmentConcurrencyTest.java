package com.morak.back.appointment.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.application.dto.AvailableTimeRequest;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'concurrency'}", loadContext = true)
class AppointmentConcurrencyTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private int threadCount = 100;
    private String teamCode = "edeneee1";
    private String appointmentCode = "appoCode";

    @Test
    void 약속잡기에_100명이_동시에_선택한다() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(
                LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.of(16, 0))
        );

        // when
        List<AvailableTimeRequest> requests = List.of(availableTimeRequest);
        for (long i = 1; i < threadCount+1; i++) {
            long memberId = i;
            executorService.submit(() -> {
                try {
                    appointmentService.selectAvailableTimes(teamCode, memberId, appointmentCode, requests);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Appointment appointment = appointmentRepository.findByCode(appointmentCode).orElseThrow();
        assertThat(appointment.getSelectedCount()).isEqualTo(threadCount);
    }
}
