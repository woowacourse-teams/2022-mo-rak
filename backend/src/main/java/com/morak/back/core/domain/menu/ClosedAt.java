package com.morak.back.core.domain.menu;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class ClosedAt {

    @Column(nullable = false)
    private LocalDateTime closedAt;

    public ClosedAt(LocalDateTime closedAt, LocalDateTime now) {
        validateFuture(closedAt, now);
        this.closedAt = closedAt;
    }

    private void validateFuture(LocalDateTime dateTime, LocalDateTime now) {
        if (dateTime.isBefore(now)) {
            throw new DomainLogicException(CustomErrorCode.PAST_CLOSED_TIME_ERROR,
                    "마감 시각" + dateTime + "은 현재" + now + "보다 나중이어야 합니다.");
        }
    }
}
