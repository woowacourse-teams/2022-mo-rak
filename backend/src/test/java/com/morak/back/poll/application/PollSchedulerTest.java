package com.morak.back.poll.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.support.ServiceTest;
import java.util.Arrays;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ServiceTest
@ActiveProfiles("master")
class PollSchedulerTest {

    private static final String TEAM_CODE = "MoraK123";
    private static final long HOST_ID = 2L;

    private final PollScheduler pollScheduler;
    private final PollRepository pollRepository;
    private final SystemTime systemTime;

    @Autowired
    public PollSchedulerTest(
            PollScheduler pollScheduler,
            PollRepository pollRepository,
            SystemTime systemTime
    ) {
        this.pollScheduler = pollScheduler;
        this.pollRepository = pollRepository;
        this.systemTime = systemTime;
    }

    @Test
    void 스케줄링에_의해_투표를_마감한다(@Autowired EntityManager entityManager) {
        // given
        Poll poll = Poll.builder()
                .teamCode(Code.generate(ignored -> TEAM_CODE))
                .hostId(HOST_ID)
                .code(Code.generate(new RandomCodeGenerator()))
                .title("투표 제목입니다.")
                .status(MenuStatus.OPEN)
                .closedAt(new ClosedAt(systemTime.now().minusMinutes(1L), systemTime.now().minusDays(1L)))
                .pollItems(Arrays.asList(
                        PollItem.builder().subject("삼겹살").build(),
                        PollItem.builder().subject("회").build(),
                        PollItem.builder().subject("이자카야").build()
                ))
                .allowedCount(2)
                .anonymous(false)
                .build();
        pollRepository.save(poll);

        // when
        pollScheduler.schedulePoll();
        entityManager.flush();
        entityManager.refresh(poll);

        // then
        assertThat(poll.getStatus()).isEqualTo(MenuStatus.CLOSED.name());
    }
}
