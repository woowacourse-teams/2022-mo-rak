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
public class Description {

    private static final int MAX_LENGTH = 1000;

    @Column(name = "description")
    private String value;

    public Description(String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_DESCRIPTION_LENGTH_ERROR,
                    "투표 선택 이유의 길이는 " + MAX_LENGTH + " 이하여야 합니다."
            );
        }
    }
}
