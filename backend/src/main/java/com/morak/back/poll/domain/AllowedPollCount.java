package com.morak.back.poll.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AllowedPollCount {

    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 10;

    /**
     * @note MAX는 특정 투표 항목이 아닌 도메인 정책 상 생성할 수 있는 최대 item 개수 입니다.
     */
    @Min(value = MIN_COUNT, message = "가능한 투표 항목 수는 최소 1개입니다.")
    @Max(value = MAX_COUNT, message = "가능한 투표 항목 수는 최대 10개입니다.")
    private Integer allowedPollCount;

    public boolean isAllowed(int pollItemCount) {
        return pollItemCount != 0 && pollItemCount <= allowedPollCount;
    }
}
