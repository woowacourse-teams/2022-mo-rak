package com.morak.back.poll.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Code {

    @Size(min = 8, max = 8, message = "코드의 길이는 8자여야 합니다.")
    private String code;
}
