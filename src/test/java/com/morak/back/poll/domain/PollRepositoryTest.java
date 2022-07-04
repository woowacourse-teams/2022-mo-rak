package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.poll.support.DomainSupplier;
import com.morak.back.support.RepositoryTest;

@RepositoryTest
class PollRepositoryTest {

    @Autowired
    private DomainSupplier supplier;

    @Autowired
    private PollRepository pollRepository;

    @DisplayName("투표를 저장한다.")
    @Test
    void savePoll() {
        // given
        Team team = supplier.supplyTeam(1L);
        Member member = supplier.supplyMember(1L);
        Poll poll = new Poll(null, team, member, "test-title", 1, false, PollStatus.OPEN, LocalDateTime.now(),
            "test-code");

        // when
        Poll savedPoll = pollRepository.save(poll);

        // then
        Assertions.assertAll(
            () -> assertThat(savedPoll).isNotNull(),
            () -> assertThat(savedPoll.getId()).isNotNull(),
            () -> assertThat(savedPoll.getTitle()).isEqualTo("test-title"),
            () -> assertThat(savedPoll.getTeam()).isNotNull(),
            () -> assertThat(savedPoll.getHost()).isNotNull()
        );
    }

}