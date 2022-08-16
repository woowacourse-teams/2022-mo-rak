package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.AppointmentStatus.OPEN;

import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.Menu;
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
public class Appointment extends BaseEntity implements Menu {

    public static final int MINUTES_UNIT = 30;
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

    @Embedded
    @Valid
    private Code code;

    @NotNull
    private LocalDateTime closedAt;

    @Formula("(SELECT COUNT(DISTINCT aat.member_id) FROM appointment_available_time as aat WHERE aat.appointment_id = id)")
    private Integer count;

    @Builder
    private Appointment(Long id, Team team, Member host, String title, String description, LocalDate startDate,
                        LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer durationHours,
                        Integer durationMinutes, Code code, LocalDateTime closedAt) {
        LocalDate validationDate = endDate;
        if (endTime.equals(LocalTime.MIDNIGHT)) {
            validationDate = validationDate.plusDays(1);
        }
        LocalDateTime validationEndDateTime = LocalDateTime.of(validationDate, endTime);
        validateLastDatetime(validationEndDateTime);
        validateClosedAtBeforeEndDate(closedAt, validationEndDateTime);

        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.description = description;
        this.datePeriod = DatePeriod.of(startDate, endDate, endTime);
        this.timePeriod = TimePeriod.of(startTime, endTime);
        this.durationMinutes = DurationMinutes.of(durationHours, durationMinutes);
        validateDurationMinutesLessThanTimePeriod(this.durationMinutes, this.timePeriod);
        this.status = OPEN;
        this.code = code;
        this.closedAt = closedAt;
    }

    private void validateLastDatetime(LocalDateTime validationEndDateTime) {
        if (validationEndDateTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_PAST_CREATE_ERROR,
                    String.format("약속잡기의 마지막 날짜와 시간(%s)은 현재보다 과거일 수 없습니다.", validationEndDateTime)
            );
        }
    }

    private void validateClosedAtBeforeEndDate(LocalDateTime closedAt, LocalDateTime endDateTime) {
        if (closedAt.isBefore(LocalDateTime.now()) || closedAt.isAfter(endDateTime)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR,
                    String.format(
                            "약속잡기의 마감 날짜/시각(%s)은 지금(%s)과 마지막 날짜/시각(%s) 사이여야 합니다.",
                            closedAt, LocalDateTime.now(), endDateTime
                    )
            );
        }
    }

    private void validateDurationMinutesLessThanTimePeriod(DurationMinutes durationMinutes, TimePeriod timePeriod) {
        if (timePeriod.isLessThanDurationMinutes(durationMinutes.getDurationMinutes())) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR,
                    String.format(
                            "진행시간(%d)은 약속잡기의 시작시간~마지막시간(%s ~ %s) 보다 짧아야 합니다.",
                            durationMinutes.getDurationMinutes(), timePeriod.getStartTime(), timePeriod.getEndTime()
                    )
            );
        }
    }

    public Integer parseHours() {
        return this.durationMinutes.parseHours();
    }

    public Integer parseMinutes() {
        return this.durationMinutes.parseMinutes();
    }

    /*
    DatePeriod를 직접 사용하면 getEndDate()의 minusDay 를 호출할 수 없습니다.
     */
    public boolean isAvailableDateRange(DatePeriod otherDatePeriod) {
        return new DatePeriod(getStartDate(), getEndDate()).isAvailableRange(otherDatePeriod);
    }

    public boolean isAvailableTimeRange(TimePeriod timePeriod) {
        return this.timePeriod.isAvailableRange(timePeriod);
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

    @Override
    public String getTeamName() {
        return getTeam().getName();
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(datePeriod.getStartDate(), timePeriod.getStartTime());
    }

    public LocalDateTime getEndDateTime() {
        return LocalDateTime.of(datePeriod.getEndDate(), timePeriod.getEndTime());
    }

    public LocalDate getStartDate() {
        return this.datePeriod.getStartDate();
    }

    public LocalDate getEndDate() {
        LocalDate endDate = this.datePeriod.getEndDate();
        if (this.timePeriod.getEndTime().equals(LocalTime.MIDNIGHT)) {
            endDate = endDate.minusDays(1);
        }
        return endDate;
    }

    public LocalTime getStartTime() {
        return this.timePeriod.getStartTime();
    }

    public LocalTime getEndTime() {
        return this.timePeriod.getEndTime();
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
}
