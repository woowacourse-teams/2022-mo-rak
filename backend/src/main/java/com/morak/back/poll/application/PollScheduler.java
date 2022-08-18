package com.morak.back.poll.application;

import com.morak.back.core.support.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollScheduler {

    private final PollService pollService;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Generated
    void schedulePoll() {
        pollService.notifyClosedByScheduled();
    }
}
