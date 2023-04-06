package com.morak.back.poll.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.application.dto.AvailableTimeRequest;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AppointmentRepository;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'concurrency'}", loadContext = true)
class PollConcurrencyTest {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollRepository pollRepository;

    private int threadCount = 100;
    private String teamCode = "edeneee1";
    private String pollCode = "pollCode";

    private ExecutorService executorService;
    private CountDownLatch countDownLatch;
    private PollResultRequest pollResultRequest;

    @BeforeEach
    void setup() {
        executorService = Executors.newFixedThreadPool(threadCount);
        countDownLatch = new CountDownLatch(threadCount);
        pollResultRequest = new PollResultRequest(1L, "좋아요");
    }

    @Test
    void 투표에_100명이_동시에_선택한다() throws InterruptedException {
        // when
        List<PollResultRequest> requests = List.of(pollResultRequest);
        for (long i = 1; i < threadCount+1; i++) {
            long memberId = i;
            executorService.submit(() -> {
                try {
                    pollService.doPoll(teamCode, memberId, pollCode, requests);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Poll poll = pollRepository.findByCode(pollCode).orElseThrow();
        assertThat(poll.getSelectedCount()).isEqualTo(threadCount);
    }

    @Test
    void 투표에_1명이_동시에_100번_선택한다() throws InterruptedException {
        // when
        List<PollResultRequest> requests = List.of(pollResultRequest);
        for (long i = 1; i < threadCount+1; i++) {
            executorService.submit(() -> {
                try {
                    pollService.doPoll(teamCode, 1L, pollCode, requests);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Poll poll = pollRepository.findByCode(pollCode).orElseThrow();
        assertThat(poll.getSelectedCount()).isEqualTo(1);
    }
}
