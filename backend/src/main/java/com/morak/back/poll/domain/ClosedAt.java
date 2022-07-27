package com.morak.back.poll.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClosedAt {

    @Future(message = "마감 시간은 미래여야 합니다.")
    private LocalDateTime closedAt;
}
