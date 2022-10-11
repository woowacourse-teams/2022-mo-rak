package com.morak.back.appointment.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Title {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    private String title;

    public Title(String title) {
        validateLength(title);
        this.title = title;
    }

    private void validateLength(String value) {
        if (!(MIN_LENGTH <= value.length() && value.length() <= MAX_LENGTH)) {
            throw new DomainLogicException(CustomErrorCode.TEMP_ERROR,
                    "제목의 길이 " + value.length() + "는 " + MIN_LENGTH + "이상, " + MAX_LENGTH + " 이하여야 합니다.");
        }
    }
}
