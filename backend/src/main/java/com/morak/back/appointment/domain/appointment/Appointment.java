package com.morak.back.appointment.domain.appointment;

import static com.morak.back.appointment.domain.appointment.AppointmentStatus.OPEN;

import com.morak.back.appointment.domain.appointment.timeconditions.ClosedAt;
import com.morak.back.appointment.domain.appointment.timeconditions.TimeConditions;
import com.morak.back.appointment.domain.availabletime.datetimeperiod.AvailableTimeDateTimePeriod;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.times.Times;
import com.morak.back.core.exception.CustomErrorCode;
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
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "약속잡기 제목은 빈 값일 수 없습니다.")
    @Size(min = 1, max = 255, message = "약속잡기 제목의 길이는 1~255자여야 합니다.")
    private String title;

    @NotNull(message = "약속잡기 설명은 null일 수 없습니다.")
    @Size(max = 1000, message = "약속잡기 설명의 길이는 0~1000자여야 합니다.")
    private String description;

    @Embedded
    private TimeConditions timeConditions;

    @NotNull(message = "약속잡기 상태는 null일 수 없습니다.")
    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus status;

    @Embedded
    @Valid
    private Code code;

    @NotNull
    private ClosedAt closedAt;

    @Formula("(SELECT COUNT(DISTINCT aat.member_id) FROM appointment_available_time as aat WHERE aat.appointment_id = id)")
    private Integer count;

    @Builder
    private Appointment(Long id, Team team, Member host, String title, String description, LocalDate startDate,
                        LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer durationHours,
                        Integer durationMinutes, Code code, LocalDateTime closedAt, Times times) {

        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.status = OPEN;
        this.code = code;
        this.description = description;
        this.timeConditions = TimeConditions.of(startDate, endDate, startTime, endTime,
                durationHours, durationMinutes, times.dateOfNow());
        this.closedAt = ClosedAt.of(closedAt, timeConditions.getEndDateTime(), times.dateTimeOfNow());
    }

    public void validateDateTimePeriod(AvailableTimeDateTimePeriod availableTimeDateTimePeriod) {
        timeConditions.validateDateTimePeriod(availableTimeDateTimePeriod);
    }

    public void close(Member member) {
        validateHost(member);
        status = status.close();
    }

    private void validateHost(Member member) {
        if (!isHost(member)) {
            throw new AppointmentAuthorizationException(
                    CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR,
                    member.getId() + "번 멤버는 " + getCode() + "코드의 약속잡기의 호스트가 아닙니다."
            );
        }
    }

    public boolean isBelongedTo(Team otherTeam) {
        return team.equals(otherTeam);
    }

    public boolean isHost(Member member) {
        return this.host.equals(member);
    }

    public Boolean isClosed() {
        return this.status.isClosed();
    }

    public int parseDurationHours() {
        return this.timeConditions.getDurationHours();
    }

    public int parseDurationMinutes() {
        return this.timeConditions.parseDurationMinutes();
    }

    public LocalDateTime getClosedAt() {
        return closedAt.getClosedAt();
    }

    public String getCode() {
        return code.getCode();
    }

    public Integer getCount() {
        if (this.count == null) {
            return NO_ONE_SELECTED;
        }
        return this.count;
    }

    public LocalDate getStartDate() {
        return this.timeConditions.getStartDate();
    }

    public LocalDate getEndDate() {
        return this.timeConditions.getEndDate();
    }

    public LocalTime getStartTime() {
        return this.timeConditions.getStartTime();
    }

    public LocalTime getEndTime() {
        return this.timeConditions.getEndTime();
    }

    public LocalDateTime getStartDateTime() {
        return this.timeConditions.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.timeConditions.getEndDateTime();
    }

    public int getDurationTime() {
        return this.timeConditions.getDurationTime();
    }

    public int getMinuteUnit() {
        return TimeConditions.MINUTE_UNIT;
    }
}
