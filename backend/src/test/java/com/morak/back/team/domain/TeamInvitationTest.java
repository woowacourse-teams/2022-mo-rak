package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.core.domain.Code;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

    @Test
    void 만료시간을_설정하지_않으면_기본시간인_2일로_설정된다() {
        // given
        LocalDateTime before = LocalDateTime.now().plusDays(2);

        // when
        TeamInvitation teamInvitation = TeamInvitation.builder()
                .code(Code.generate(length -> "abcd1234"))
                .build();

        // then
        LocalDateTime after = LocalDateTime.now().plusDays(2);

        assertThat(teamInvitation.getExpiredAt()).isBetween(before, after);
    }

    @Test
    void 만료되지_않은_시간인지_확인한다() {
        // given
        TeamInvitation teamInvitation = TeamInvitation.builder()
                .code(Code.generate(length -> "abcd1234"))
                .expiredAt(ExpiredTime.withMinute(30L))
                .build();

        // when
        boolean isExpired = teamInvitation.isExpired();

        // then
        assertThat(isExpired).isFalse();
    }

    @Test
    void 만료된_시간인지_확인한다() {
        // given
        TeamInvitation teamInvitation = TeamInvitation.builder()
                .code(Code.generate(length -> "abcd1234"))
                .expiredAt(ExpiredTime.withMinute(-10L))
                .build();

        // when
        boolean isExpired = teamInvitation.isExpired();

        // then
        assertThat(isExpired).isTrue();
    }
}
