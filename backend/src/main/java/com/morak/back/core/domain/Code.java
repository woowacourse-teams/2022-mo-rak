package com.morak.back.core.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Code {

    private static final int LENGTH = 8;

    @NotBlank(message = "code는 blank 일 수 없습니다.")
    @Size(min = LENGTH, max = LENGTH, message = "코드의 길이는 8자여야 합니다.")
    private String code;

    public static Code generate(CodeGenerator generator) {
        return new Code(generator.generate(LENGTH));
    }
}
