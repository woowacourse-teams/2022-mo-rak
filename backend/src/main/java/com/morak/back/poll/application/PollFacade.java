package com.morak.back.poll.application;

import com.morak.back.appointment.application.dto.AvailableTimeRequest;
import com.morak.back.poll.application.dto.PollResultRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollFacade {

    private final PollService pollService;
    private final RedissonClient redissonClient;

    public void doPoll(String teamCode, long memberId, String pollCode,
                                     List<PollResultRequest> requests) {
        RLock lock = redissonClient.getLock(String.format("first:%s", pollCode));
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("redisson getLock timeout");
            }
            pollService.doPoll(teamCode, memberId, pollCode, requests);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
