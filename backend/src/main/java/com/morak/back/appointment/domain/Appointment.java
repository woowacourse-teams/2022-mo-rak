package com.morak.back.appointment.domain;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR;

import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import com.morak.back.poll.domain.BaseEntity;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    public static final int MINUTES_UNIT = 30;
    private static final int NO_ONE_SELECTED = 0;

    @Embedded
    private Menu menu;

    @Embedded
    private DatePeriod datePeriod;

    @Embedded
    private TimePeriod timePeriod;

    @Embedded
    private DurationMinutes durationMinutes;

    @ElementCollection
    @CollectionTable(
            name = "appointment_available_time",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    private Set<AvailableTime> availableTimes = new HashSet<>();

    @Formula("(SELECT COUNT(DISTINCT aat.member_id) FROM appointment_available_time as aat WHERE aat.appointment_id = id)")
    private Integer count;

    @Builder
    private Appointment(Long id, Team team, Member host, String title, String description, LocalDate startDate,
                        LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer durationHours,
                        Integer durationMinutes, Code code, LocalDateTime closedAt, LocalDateTime now) {
        super(id);
        this.menu = new Menu(team, host, code, title, description, MenuStatus.OPEN,
                new AppointmentClosedAt(closedAt, now, endDate, endTime));
        this.datePeriod = new DatePeriod(startDate, endDate, now.toLocalDate());
        this.timePeriod = new TimePeriod(startTime, endTime);
        this.durationMinutes = DurationMinutes.of(durationHours, durationMinutes);
        validateDurationAndPeriod(this.timePeriod, this.durationMinutes);
    }

    private void validateDurationAndPeriod(TimePeriod timePeriod, DurationMinutes durationMinutes) {
        if (durationMinutes.isLongerThan(timePeriod.getDuration())) {
            throw new DomainLogicException(
                    CustomErrorCode.TEMP_ERROR,
                    "진행 시간" + durationMinutes + "은 약속 시간보다 짧아야 합니다"
            );
        }
    }

    public void selectAvailableTime(Set<LocalDateTime> localDateTimes, Member member, LocalDateTime now) {
        validateStatus();

        Set<AvailableTime> availableTimes = new HashSet<>();
        for (LocalDateTime dateTime : localDateTimes) {
            validateSelectTime(now, dateTime);
            availableTimes.add(AvailableTime.builder().member(member).startDateTime(dateTime).build());
        }

        this.availableTimes.removeIf(availableTime -> availableTime.getMember().equals(member));
        this.availableTimes.addAll(availableTimes);
    }

    private void validateSelectTime(LocalDateTime now, LocalDateTime dateTime) {
        if (!isDateTimeBetween(dateTime, now)) {
            throw new AppointmentDomainLogicException(AVAILABLETIME_OUT_OF_RANGE_ERROR,
                    getCode() + "코드의 약속잡기에" + dateTime + "은 선택할 수 없는 시간입니다.");
        }
    }

    private void validateStatus() {
        if (isClosed()) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR,
                    getCode() + "코드의 약속잡기는 마감되었습니다."
            );
        }
    }

    private boolean isDateTimeBetween(LocalDateTime dateTime, LocalDateTime now) {
        return this.datePeriod.isBetween(dateTime.toLocalDate())
                && this.timePeriod.isBetween(dateTime.toLocalTime())
                && now.isBefore(dateTime);
    }

    public Integer parseHours() {
        return this.durationMinutes.parseHours();
    }

    public Integer parseMinutes() {
        return this.durationMinutes.parseMinutes();
    }

    public void close(Member member) {
        validateHost(member);
        menu.close();
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
        return menu.getTeam().equals(otherTeam);
    }

    public boolean isHost(Member member) {
        return this.menu.isHost(member);
    }

    public Boolean isClosed() {
        return this.menu.isClosed();
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

    public String getCode() {
        return menu.getCode();
    }

    public Integer getCount() {
        if (this.count == null) {
            return NO_ONE_SELECTED;
        }
        return this.count;
    }

    public String getTitle() {
        return menu.getTitle();
    }

    public String getDescription() {
        return menu.getDescription();
    }


    public LocalDateTime getClosedAt() {
        return menu.getClosedAt();
    }


    public Team getTeam() {
        return menu.getTeam();
    }

    public MenuStatus getStatus() {
        return menu.getStatus();
    }

    public Member getHost() {
        return menu.getHost();
    }

    public List<AppointmentTime> getAppointmentTimes() {
        LocalTime startTime = timePeriod.getStartTime();
        LocalTime endTime = timePeriod.getEndTime();
        long duration = (long) durationMinutes.getDurationMinutes() - MINUTES_UNIT;

        LocalDate date = datePeriod.getStartDate();
        List<AppointmentTime> times = new ArrayList<>();
        while (!date.isAfter(datePeriod.getEndDate())) {
            while (!startTime.plusMinutes(duration).isAfter(endTime)) {
                times.add(new AppointmentTime(LocalDateTime.of(date, startTime), durationMinutes.getDurationMinutes()));
                startTime = startTime.plusMinutes(MINUTES_UNIT);
            }
            startTime = timePeriod.getStartTime();
            date = date.plusDays(1L);
        }

        return times;
    }
}
