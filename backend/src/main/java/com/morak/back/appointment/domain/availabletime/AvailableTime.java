package com.morak.back.appointment.domain.availabletime;

import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.appointment.domain.availabletime.datetimeperiod.AvailableTimeDateTimePeriod;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.BaseEntity;
import java.time.LocalDateTime;
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
    private AvailableTimeDateTimePeriod availableTimeDateTimePeriod;

    @Builder
    private AvailableTime(Long id,
                          Appointment appointment, Member member,
                          LocalDateTime startDateTime, LocalDateTime endDateTime,
                          LocalDateTime now) {

        this.availableTimeDateTimePeriod = AvailableTimeDateTimePeriod.of(startDateTime, endDateTime, now);
        appointment.validateDateTimePeriod(availableTimeDateTimePeriod);

        this.id = id;
        this.appointment = appointment;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        AvailableTime that = (AvailableTime) o;
        return Objects.equals(id, that.id) && Objects.equals(appointment, that.appointment)
                && Objects.equals(member, that.member) && Objects.equals(availableTimeDateTimePeriod,
                that.availableTimeDateTimePeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appointment, member, availableTimeDateTimePeriod);
    }
}
