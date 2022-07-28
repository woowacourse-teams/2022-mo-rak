package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.AppointmentStatus.OPEN;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.poll.domain.BaseEntity;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    private static final int NO_ONE_SELECTED = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "약속잡기는 팀에 소속되어 있어야 합니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @NotNull(message = "약속잡기는 호스트가 있어야 합니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @NotNull(message = "약속잡기 제목은 null일 수 없습니다.")
    @Size(min = 1, max = 255, message = "약속잡기 제목의 길이는 1~255자여야 합니다.")
    private String title;

    @NotNull(message = "약속잡기 설명은 null일 수 없습니다.")
    @Size(max = 1000, message = "약속잡기 설명의 길이는 0~1000자여야 합니다.")
    private String description;

    @Embedded
    @Valid
    private DatePeriod datePeriod;

    @Embedded
    @Valid
    private TimePeriod timePeriod;

    @Embedded
    @Valid
    private DurationMinutes durationMinutes;

    @NotNull(message = "약속잡기 상태는 null일 수 없습니다.")
    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus status;

    private String code;

    private LocalDateTime closedAt;

    @Formula("(SELECT COUNT(DISTINCT aat.member_id) FROM appointment_available_time aat WHERE aat.appointment_id = id)")
    private Integer count;

    @Builder
    public Appointment(Long id, Team team, Member host, String title, String description, LocalDate startDate,
                       LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer durationHours,
                       Integer durationMinutes, String code, LocalDateTime closedAt) {
        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.description = description;
        this.datePeriod = new DatePeriod(startDate, endDate);
        this.timePeriod = new TimePeriod(startTime, endTime);
        this.durationMinutes = new DurationMinutes(durationHours, durationMinutes);
        this.status = OPEN;
        // TODO: 2022/07/27 VO로 변경해야함!!
        this.code = CodeGenerator.createRandomCode(8);
        this.closedAt = LocalDateTime.now().plusMonths(1);
    }

    public Integer parseHours() {
        return this.durationMinutes.parseHours();
    }

    public Integer parseMinutes() {
        return this.durationMinutes.parseMinutes();
    }

    public Boolean isClosed() {
        return this.status.isClosed();
    }

    public LocalDate getStartDate() {
        return this.datePeriod.getStartDate();
    }

    public LocalDate getEndDate() {
        return this.datePeriod.getEndDate();
    }

    public LocalTime getStartTime() {
        return this.timePeriod.getStartTime();
    }

    public LocalTime getEndTime() {
        return this.timePeriod.getEndTime();
    }

    public Integer getCount() {
        if (this.count == null) {
            return NO_ONE_SELECTED;
        }
        return this.count;
    }

    public void validateAvailableTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.datePeriod.validateAvailableDateRange(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        this.timePeriod.validateAvailableTimeRange(startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }

    public void close(Member member) {
        validateHost(member);
        status = status.close();
    }

    public void validateHost(Member member) {
        if (!host.equals(member)) {
            throw new InvalidRequestException(member.getId() + "번 멤버는 " + id + "번 약속잡기의 호스트가 아닙니다.");
        }
    }
}
