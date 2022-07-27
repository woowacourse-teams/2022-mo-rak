package com.morak.back.poll.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Code {

    @NotEmpty(message = "code는 null 일 수 없습니다.")
    @Size(min = 8, max = 8, message = "코드의 길이는 8자여야 합니다.")
    private String code;
}
