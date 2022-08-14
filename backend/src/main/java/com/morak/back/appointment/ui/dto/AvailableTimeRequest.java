package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AvailableTime;
import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime start;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime end;

    public AvailableTime toAvailableTime(Member member, Appointment appointment) {
        return AvailableTime.builder()
                .member(member)
                .appointment(appointment)
                .startDateTime(this.start)
                .endDateTime(this.end)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableTimeRequest that = (AvailableTimeRequest) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
