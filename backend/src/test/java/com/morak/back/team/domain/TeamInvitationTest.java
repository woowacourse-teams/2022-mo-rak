package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.core.domain.Code;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

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
