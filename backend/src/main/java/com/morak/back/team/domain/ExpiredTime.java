package com.morak.back.team.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExpiredTime {

    @NotNull(message = "만료되는 시간은 null일 수 없습니다.")
    private LocalDateTime expiredAt;

    public static ExpiredTime withMinute(long minutes) {
        return new ExpiredTime(LocalDateTime.now().plusMinutes(minutes));
    }

    public boolean isBefore(LocalDateTime other) {
        return expiredAt.isBefore(other);
    }
}
