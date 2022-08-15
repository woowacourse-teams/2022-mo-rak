package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.Appointment.MINUTES_UNIT;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.poll.domain.BaseEntity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "appointment_available_time")
public class AvailableTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "약속잡기 가능 시간은 약속잡기에 소속되어 있어야합니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Appointment appointment;

    @NotNull(message = "약속잡기 가능 시간은 호스트가 있어야합니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    @Valid
    private DateTimePeriod dateTimePeriod;

    @Builder
    private AvailableTime(Long id, Appointment appointment, Member member,
                          LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTimePeriod dateTimePeriod = DateTimePeriod.of(startDateTime, endDateTime, MINUTES_UNIT);
        validateAvailableTimeRange(appointment, dateTimePeriod);

        this.id = id;
        this.appointment = appointment;
        this.member = member;
        this.dateTimePeriod = dateTimePeriod;
    }

    private void validateAvailableTimeRange(Appointment appointment, DateTimePeriod createDateTimePeriod) {
        validateDateRange(appointment, createDateTimePeriod);
        validateTimeRange(appointment, createDateTimePeriod);
    }

    private void validateDateRange(Appointment appointment, DateTimePeriod createDateTimePeriod) {
        LocalTime createLocalTime = createDateTimePeriod.getEndDateTime().toLocalTime();

        if (!appointment.isAvailableDateRange(createDateTimePeriod.toDatePeriod(createLocalTime))) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR,
                String.format(
                    "%s 코드 투표의 약속잡기선택날짜 %s 는 약속잡기날짜 %s 이내여야 합니다.",
                    appointment.getCode(),
                    createDateTimePeriod.toDatePeriod(createLocalTime),
                    appointment.getDatePeriod()
                )
            );
        }
    }

    private void validateTimeRange(Appointment appointment, DateTimePeriod createDateTimePeriod) {
        if (!appointment.isAvailableTimeRange(createDateTimePeriod.toTimePeriod())) {
            throw new AppointmentDomainLogicException(
                CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR,
                String.format(
                    "%s 코드 투표의 약속잡기 선택시간 %s 는 %s 이내여야 합니다.",
                    appointment.getCode(),
                    createDateTimePeriod.toTimePeriod(),
                    appointment.getTimePeriod()
                )
            );
        }
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        AvailableTime that = (AvailableTime) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getAppointment(),
                that.getAppointment()) && Objects.equals(getMember(), that.getMember())
                && Objects.equals(getDateTimePeriod(), that.getDateTimePeriod());
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(getId(), getAppointment(), getMember(), getDateTimePeriod());
    }
}
