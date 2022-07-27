package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.BaseEntity;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    private static final int MINUTES_OF_HOUR = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer durationMinutes;

    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus status;

    private String code;

    private LocalDateTime closedAt;

    @Builder
    public Appointment(Long id, Team team, Member host, String title, String description, LocalDate startDate,
                       LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer durationMinutes,
                       AppointmentStatus status, String code, LocalDateTime closedAt) {
        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.code = code;
        this.closedAt = closedAt;
    }

    public Integer parseHours() {
        return this.durationMinutes / MINUTES_OF_HOUR;
    }

    public Integer parseMinutes() {
        return this.durationMinutes % MINUTES_OF_HOUR;
    }

    public Boolean isClosed() {
        return this.status.isClosed();
    }

    // TODO: 2022/07/27 formula 적용해야함!
    public Integer getCount() {
        return 1;
    }
}
