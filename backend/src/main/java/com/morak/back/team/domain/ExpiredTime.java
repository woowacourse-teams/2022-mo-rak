package com.morak.back.team.domain;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.exception.TeamDomainLogicException;
import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ExpiredTime {

    private static final long DEFAULT_EXPIRED_MINUTES = 2 * 24 * 60L;

    private LocalDateTime expiredAt;

    public ExpiredTime(SystemTime systemTime) {
        this(DEFAULT_EXPIRED_MINUTES, systemTime);
    }

    public ExpiredTime(long minutes, SystemTime systemTime) {
        this.expiredAt = systemTime.now().plusMinutes(minutes);
    }

    public ExpiredTime(LocalDateTime expiredAt, SystemTime systemTime) {
        validateFuture(expiredAt, systemTime);
        this.expiredAt = expiredAt;
    }

    private void validateMinutesPositive(long minutes) {
        if (minutes < 0) {
            throw new TeamDomainLogicException(
                    CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR,
                    "초대코드 만료 시간은 현재보다 미래여야 합니다."
            );
        }
    }

    private void validateFuture(LocalDateTime expiredAt, SystemTime systemTime) {
        LocalDateTime now = systemTime.now();
        if (expiredAt.isBefore(now)) {
            throw new TeamDomainLogicException(
                    CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR,
                    "초대코드 만료 시간" + expiredAt + "은 현재" + now + "보다 이후여야 합니다."
            );
        }
    }

    public boolean isBefore(LocalDateTime other) {
        return expiredAt.isBefore(other);
    }
}
