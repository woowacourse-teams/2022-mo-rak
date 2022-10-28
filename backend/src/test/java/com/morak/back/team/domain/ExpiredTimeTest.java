package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.exception.TeamDomainLogicException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ExpiredTimeTest {

    private SystemTime systemTime = new SystemTime(LocalDateTime.now());

    @Test
    void 만료시간을_설정하지_않으면_기본시간인_2일로_설정된다() {
        // given & when
        ExpiredTime expiredTime = new ExpiredTime(systemTime);

        // then
        assertThat(expiredTime.getExpiredAt()).isEqualToIgnoringSeconds(systemTime.now().plusDays(2));
    }

    @Test
    void 특정시간_이후의_만료시각을_설정한다() {
        // given
        long minutes = 30;

        // when
        ExpiredTime expiredTime = new ExpiredTime(minutes, systemTime);

        // then
        assertThat(expiredTime.getExpiredAt()).isEqualToIgnoringSeconds(systemTime.now().plusMinutes(minutes));
    }

    @Test
    void 과거의_만료시각을_설정할_수_없다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(1);
        // when & then
        assertThatThrownBy(() -> new ExpiredTime(expiredAt, systemTime))
                .isInstanceOf(TeamDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR);
    }

    @Test
    void 만료시각이_과거가_아니라면_false를_반환한다() {
        ExpiredTime expiredTime = new ExpiredTime(systemTime);

        boolean isBefore = expiredTime.isBefore(systemTime.now().minusDays(5));

        assertThat(isBefore).isFalse();
    }

    @Test
    void 만료시각이_과거라면_true를_반환한다() {
        ExpiredTime expiredTime = new ExpiredTime(systemTime);

        boolean isBefore = expiredTime.isBefore(systemTime.now().plusDays(5));

        assertThat(isBefore).isTrue();
    }
}
