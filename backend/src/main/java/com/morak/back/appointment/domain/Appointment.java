package com.morak.back.appointment.domain;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_OUT_OF_RANGE_ERROR;

import com.morak.back.appointment.domain.dateperiod.DatePeriod;
import com.morak.back.appointment.domain.recommend.AppointmentTime;
import com.morak.back.appointment.domain.timeperiod.TimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.BaseRootEntity;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.Menu;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.core.domain.menu.Title;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Appointment extends BaseRootEntity<Appointment> {

    public static final int MINUTES_UNIT = 30;

    @Embedded
    private Menu menu;

    @Embedded
    private SubTitle subTitle;

    @Embedded
    private DatePeriod datePeriod;

    @Embedded
    private TimePeriod timePeriod;

    @Embedded
    private DurationMinutes durationMinutes;

    @Embedded
    private AvailableTimes availableTimes;

    private long selectedCount;

    @Builder
    private Appointment(Long id, Code teamCode, Long hostId, String title, String subTitle, LocalDate startDate,
                        LocalDate endDate, LocalTime startTime, LocalTime endTime, int durationHours,
                        int durationMinutes, Code code, LocalDateTime closedAt, LocalDateTime now) {
        super(id);
        this.menu = new Menu(teamCode, hostId, code, new Title(title), MenuStatus.OPEN,
                new AppointmentClosedAt(closedAt, now, endDate, endTime));
        this.subTitle = new SubTitle(subTitle);
        this.datePeriod = new DatePeriod(startDate, endDate, now.toLocalDate());
        this.timePeriod = new TimePeriod(startTime, endTime);
        this.durationMinutes = DurationMinutes.of(durationHours, durationMinutes);
        validateDurationAndPeriod(this.timePeriod, this.durationMinutes);
        this.availableTimes = new AvailableTimes();
        this.selectedCount = 0;
        registerEvent(AppointmentEvent.from(menu));
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
        validateSelectTime(now, localDateTimes);
        countUpIfNotExists(memberId);

        this.availableTimes.select(localDateTimes, memberId);
    }

    private void validateOpen() {
        if (menu.isClosed()) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_ALREADY_CLOSED_ERROR,
                    menu.getCode() + "코드의 약속잡기는 마감되었습니다."
            );
        }
    }

    private void countUpIfNotExists(Long memberId) {
        if (!this.availableTimes.hasMember(memberId)) {
            this.selectedCount++;
        }
    }

    private void validateSelectTime(LocalDateTime now, Set<LocalDateTime> dateTimes) {
        if (dateTimes.stream()
                .anyMatch(dateTime -> !isDateTimeBetween(dateTime, now))) {
            throw new AppointmentDomainLogicException(AVAILABLETIME_OUT_OF_RANGE_ERROR,
                    menu.getCode() + "코드의 약속잡기에" + dateTimes + "는 선택할 수 없는 시간입니다.");
        }
    }

    private boolean isDateTimeBetween(LocalDateTime dateTime, LocalDateTime now) {
        return this.datePeriod.isBetween(dateTime.toLocalDate())
                && this.timePeriod.isBetween(dateTime.toLocalTime())
                && now.isBefore(dateTime);
    }

    public List<AppointmentTime> getAppointmentTimes() {
        List<AppointmentTime> times = new ArrayList<>();

        LocalDate endDate = datePeriod.getEndDate();
        LocalDate date = datePeriod.getStartDate();

        while (!date.isAfter(endDate)) {
            times.addAll(getAppointmentPerDate(date));
            date = date.plusDays(1L);
        }
        return times;
    }

    private List<AppointmentTime> getAppointmentPerDate(LocalDate date) {
        List<AppointmentTime> times = new ArrayList<>();

        int durationMinutes = this.durationMinutes.getDurationMinutes();
        LocalDateTime startDateTime = LocalDateTime.of(date, timePeriod.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(date, timePeriod.getEndTime());

        int duration = durationMinutes - MINUTES_UNIT;
        while (!startDateTime.plusMinutes(duration).isAfter(endDateTime)) {
            times.add(new AppointmentTime(startDateTime, durationMinutes));
            startDateTime = startDateTime.plusMinutes(MINUTES_UNIT);
        }
        return times;
    }

    public void close(Long memberId) {
        menu.close(memberId);
        registerEvent(AppointmentEvent.from(menu));
    }

    public boolean isHost(Long memberId) {
        return menu.isHost(memberId);
    }

    public boolean isBelongedTo(String teamCode) {
        return menu.isBelongedTo(teamCode);
    }

    public int parseHours() {
        return durationMinutes.parseHours();
    }

    public int parseMinutes() {
        return durationMinutes.parseMinutes();
    }

    public String getCode() {
        return menu.getCode();
    }

    public String getTeamCode() {
        return menu.getTeamCode();
    }

    public Long getHostId() {
        return menu.getHostId();
    }

    public String getTitle() {
        return menu.getTitle();
    }

    public String getSubTitle() {
        return this.subTitle.getSubTitle();
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

    public LocalDateTime getClosedAt() {
        return this.menu.getClosedAt();
    }

    public boolean isClosed() {
        return this.menu.isClosed();
    }

    public String getStatus() {
        return this.menu.getStatus();
    }

    public long getSelectedCount() {
        return this.selectedCount;
    }

    public Set<AvailableTime> getAvailableTimes() {
        return this.availableTimes.getAvailableTimes();
    }
}
