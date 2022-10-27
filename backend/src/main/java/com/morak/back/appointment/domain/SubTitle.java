package com.morak.back.appointment.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class SubTitle {

    private static final int MAX_LENGTH = 255;

    private String subTitle;

    public SubTitle(String subTitle) {
        validateLength(subTitle);
        this.subTitle = subTitle;
    }

    private void validateLength(String value) {
        if (!(value.length() <= MAX_LENGTH)) {
            throw new DomainLogicException(CustomErrorCode.DESCRIPTION_OUT_OF_LENGTH_ERROR,
                    "설명의 길이 " + value.length() + "는 " + MAX_LENGTH + " 이하여야 합니다.");
        }
    }
}
