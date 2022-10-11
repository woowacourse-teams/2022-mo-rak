package com.morak.back.appointment.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Description {

    private static final int MAX_LENGTH = 1000;

    private String description;

    public Description(String description) {
        validateLength(description);
        this.description = description;
    }

    private void validateLength(String value) {
        if (!(value.length() <= MAX_LENGTH)) {
            throw new DomainLogicException(CustomErrorCode.TEMP_ERROR,
                    "설명의 길이 " + value.length() + "는 " + MAX_LENGTH + " 이하여야 합니다.");
        }
    }
}
