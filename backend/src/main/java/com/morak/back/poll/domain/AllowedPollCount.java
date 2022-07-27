package com.morak.back.poll.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AllowedPollCount {

    @Min(value = 1, message = "가능한 투표 항목 수는 1 이상이어야 합니다.")
    private Integer allowedPollCount;

    public boolean isAllowed(int pollItemCount) {
        return pollItemCount != 0 && pollItemCount <= allowedPollCount;
    }
}
