package com.morak.back.poll.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class AllowedCount {

    @Column(name = "allowed_count")
    private int value;

    public AllowedCount(int value) {
        validateAllowedCount(value);
        this.value = value;
    }

    private void validateAllowedCount(int allowedCount) {
        if (allowedCount < 1) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ALLOWED_COUNT_MIN_ERROR,
                    "투표 선택 허용 개수(" + allowedCount + ")는 1개 이상이어야 합니다."
            );
        }
    }

    public boolean isGreaterThan(int itemCount) {
        return this.value > itemCount;
    }

    public boolean isGreaterThanOrEqual(int itemCount) {
        return this.value >= itemCount;
    }
}
