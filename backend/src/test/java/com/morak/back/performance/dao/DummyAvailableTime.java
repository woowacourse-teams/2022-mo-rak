package com.morak.back.performance.dao;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DummyAvailableTime {

    private Long appointmentId;
    private Long memberId;
    private LocalDateTime startDateTime;
    private LocalDateTime createdAt;
}
