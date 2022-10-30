package com.morak.back.poll.application;

import com.morak.back.core.support.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("master")
public class PollScheduler {

    private final PollService pollService;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Generated
    public void schedulePoll() {
        pollService.closeAllBeforeNow();
    }
}
