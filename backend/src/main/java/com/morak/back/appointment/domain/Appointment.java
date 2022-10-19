package com.morak.back.appointment.domain;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR;

import com.morak.back.appointment.domain.dateperiod.DatePeriod;
import com.morak.back.appointment.domain.menu.Menu;
import com.morak.back.appointment.domain.menu.MenuStatus;
import com.morak.back.appointment.domain.recommend.AppointmentTime;
import com.morak.back.appointment.domain.timeperiod.TimePeriod;
import com.morak.back.appointment.exception.AppointmentAuthorizationException;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.domain.BaseEntity;
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
import lombok.experimental.Delegate;

@Entity
@NoArgsConstructor
public class Appointment extends BaseEntity {

    public static final int MINUTES_UNIT = 30;

    @Getter
    @ElementCollection
    @CollectionTable(
            name = "appointment_available_time",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    private final Set<AvailableTime> availableTimes = new HashSet<>();

    @Embedded
    @Delegate
    private Menu menu;

    @Embedded
    @Delegate
    private DatePeriod datePeriod;

    @Embedded
    @Delegate
    private TimePeriod timePeriod;

    @Embedded
    @Delegate
    private DurationMinutes durationMinutes;

    @Builder
    private Appointment(Long id, String teamCode, Long hostId, String title, String description, LocalDate startDate,
                        LocalDate endDate, LocalTime startTime, LocalTime endTime, int durationHours,
                        int durationMinutes, Code code, LocalDateTime closedAt, LocalDateTime now) {
        super(id);
        this.menu = new Menu(teamCode, hostId, code, title, description, MenuStatus.OPEN,
                new AppointmentClosedAt(closedAt, now, endDate, endTime));
        this.datePeriod = new DatePeriod(startDate, endDate, now.toLocalDate());
        this.timePeriod = new TimePeriod(startTime, endTime);
        this.durationMinutes = DurationMinutes.of(durationHours, durationMinutes);
        validateDurationAndPeriod(this.timePeriod, this.durationMinutes);
    }

    private void validateDurationAndPeriod(TimePeriod timePeriod, DurationMinutes durationMinutes) {
        if (durationMinutes.isLongerThan(timePeriod.getDuration())) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR,
                    String.format(
                            "진행시간 %s 은 약속시간 (%s ~ %s) 보다 짧아야 합니다.",
                            durationMinutes, timePeriod.getStartTime(), timePeriod.getEndTime()
                    )
            );
        }
    }

    public void selectAvailableTime(Set<LocalDateTime> localDateTimes, Long memberId, LocalDateTime now) {
        validateOpen();

        Set<AvailableTime> availableTimes = new HashSet<>();
        for (LocalDateTime dateTime : localDateTimes) {
            validateSelectTime(now, dateTime);
            availableTimes.add(AvailableTime.builder().memberId(memberId).startDateTime(dateTime).build());
        }

        this.availableTimes.removeIf(availableTime -> availableTime.getMemberId().equals(memberId));
        this.availableTimes.addAll(availableTimes);
    }

    private void validateOpen() {
        if (isClosed()) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR,
                    getCode() + "코드의 약속잡기는 마감되었습니다."
            );
        }
    }

    public void close(Long memberId) {
        validateHost(memberId);
        menu.close();
    }

    private void validateHost(Long memberId) {
        if (!isHost(memberId)) {
            throw new AppointmentAuthorizationException(
                    CustomErrorCode.APPOINTMENT_MEMBER_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + getCode() + "코드의 약속잡기의 호스트가 아닙니다."
            );
        }
    }

    private void validateSelectTime(LocalDateTime now, LocalDateTime dateTime) {
        if (!isDateTimeBetween(dateTime, now)) {
            throw new AppointmentDomainLogicException(AVAILABLETIME_OUT_OF_RANGE_ERROR,
                    getCode() + "코드의 약속잡기에" + dateTime + "은 선택할 수 없는 시간입니다.");
        }
    }

    private boolean isDateTimeBetween(LocalDateTime dateTime, LocalDateTime now) {
        return this.datePeriod.isBetween(dateTime.toLocalDate())
                && this.timePeriod.isBetween(dateTime.toLocalTime())
                && now.isBefore(dateTime);
    }

    public int getCount() {
        return (int) availableTimes.stream()
                .map(AvailableTime::getMemberId)
                .distinct()
                .count();
    }

    // TODO: 2022/10/17 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!여기까지
    public List<AppointmentTime> getAppointmentTimes() {


        List<AppointmentTime> times = new ArrayList<>();

        LocalDate endDate = datePeriod.getEndDate();
        LocalDate date = datePeriod.getStartDate();
        for (; !date.isAfter(endDate); date = date.plusDays(1L)) {
            extracted(date);
        }

        return times;
    }

    private List<AppointmentTime> extracted(LocalDate date) {
        List<AppointmentTime> times = new ArrayList<>();

        LocalTime startTime = timePeriod.getStartTime();
        LocalTime endTime = timePeriod.getEndTime();
        int durationMinutes = this.durationMinutes.getDurationMinutes();
        long duration = (long) durationMinutes - MINUTES_UNIT;
        for (; !startTime.plusMinutes(duration).isAfter(endTime); startTime = startTime.plusMinutes(MINUTES_UNIT)) {
            times.add(new AppointmentTime(LocalDateTime.of(date, startTime), durationMinutes));
        }
        return times;
    }
}
