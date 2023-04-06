package com.morak.back.appointment.application;

import com.morak.back.appointment.application.dto.AvailableTimeRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentFacade {

    private final AppointmentService appointmentService;
    private final RedissonClient redissonClient;

    public void selectAvailableTimes(String teamCode, long memberId, String appointmentCode,
                                                       List<AvailableTimeRequest> requests) {
        RLock lock = redissonClient.getLock(String.format("first:%s", appointmentCode));
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("redisson getLock timeout");
            }
            appointmentService.selectAvailableTimes(teamCode, memberId, appointmentCode, requests);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
