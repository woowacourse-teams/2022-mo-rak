package com.morak.back.brandnew.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class NewAllowedCount {

    private int value;

    public NewAllowedCount(int value) {
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

    public boolean isGraterThan(int itemCount) {
        return this.value > itemCount;
    }

    public boolean isLessThan(int itemCount) {
        return this.value < itemCount;
    }
}
