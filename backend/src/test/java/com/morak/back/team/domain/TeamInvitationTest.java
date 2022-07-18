package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

    @DisplayName("만료되지 않은 시간인지 확인한다.")
    @Test
    void isExpired() {
        // given
        TeamInvitation teamInvitation =
                new TeamInvitation(null, null, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(30L));
        // when
        boolean isExpired = teamInvitation.isExpired();
        // then
        assertThat(isExpired).isFalse();
    }

    @DisplayName("만료된 시간인지 확인한다.")
    @Test
    void isNotExpired() {
        // given
        TeamInvitation teamInvitation =
                new TeamInvitation(null, null, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(-30L));
        // when
        boolean isExpired = teamInvitation.isExpired();
        // then
        assertThat(isExpired).isTrue();
    }
}