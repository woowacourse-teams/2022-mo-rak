package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.domain.Code;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

    private SystemTime systemTime = new SystemTime(LocalDateTime.of(2022, 10, 25, 3, 28));

    @Test
    void 만료되지_않은_시간인지_확인한다() {
        // given
        TeamInvitation teamInvitation = TeamInvitation.builder()
                .code(Code.generate(length -> "abcd1234"))
                .expiredAt(new ExpiredTime(30L, systemTime))
                .build();

        // when
        boolean isExpired = teamInvitation.isExpired(systemTime);

        // then
        assertThat(isExpired).isFalse();
    }

    @Test
    void 만료된_시간인지_확인한다() {
        // given
        SystemTime oldSystemTime = new SystemTime(systemTime.now().minusMinutes(10));
        TeamInvitation teamInvitation = TeamInvitation.builder()
                .code(Code.generate(length -> "abcd1234"))
                .expiredAt(new ExpiredTime(oldSystemTime.now().plusMinutes(5), oldSystemTime))
                .build();

        // when
        boolean isExpired = teamInvitation.isExpired(systemTime);

        // then
        assertThat(isExpired).isTrue();
    }
}
