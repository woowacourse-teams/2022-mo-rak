package com.morak.back.core.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Code {

    private static final int LENGTH = 8;

    @Column(nullable = false)
    private String code;

    private Code(String code) {
        validateLength(code);
        this.code = code;
    }

    public static Code generate(CodeGenerator generator) {
        return new Code(generator.generate(LENGTH));
    }

    private void validateLength(String value) {
        if (value.length() != LENGTH) {
            throw new DomainLogicException(
                    CustomErrorCode.CODE_LENGTH_ERROR, "코드" + value + "의 길이는 " + LENGTH + "자여야 합니다."
            );
        }
    }

    public boolean isEqualTo(final String other) {
        return this.code.equals(other);
    }
}
