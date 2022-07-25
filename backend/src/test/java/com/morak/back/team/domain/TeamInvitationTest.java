package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

    @Test
    void 만료되지_않은_시간인지_확인한다() {
        // given
        TeamInvitation teamInvitation =
                new TeamInvitation(null, null, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(30L));
        // when
        boolean isExpired = teamInvitation.isExpired();
        // then
        assertThat(isExpired).isFalse();
    }

    @Test
    void 만료된_시간인지_확인한다() {
        // given
        TeamInvitation teamInvitation =
                new TeamInvitation(null, null, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(-30L));
        // when
        boolean isExpired = teamInvitation.isExpired();
        // then
        assertThat(isExpired).isTrue();
    }
}