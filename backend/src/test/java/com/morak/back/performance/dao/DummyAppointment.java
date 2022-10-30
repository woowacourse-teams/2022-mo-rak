package com.morak.back.performance.dao;

import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.poll.domain.PollStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DummyAppointment {

    private String teamCode;
    private Long hostId;
    private String title;
    private String subTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private MenuStatus status;
    private String code;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
