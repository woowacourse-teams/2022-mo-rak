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
public class Subject {
    @Size(min = 1, max = 255, message = "투표 항목 주제의 길이는 1~255자여아 합니다.")
    private String subject;
}
