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
public class Description {

    @Size(max = 255, message = "description은 255자 이하여야합니다.")
    private String description;
}
