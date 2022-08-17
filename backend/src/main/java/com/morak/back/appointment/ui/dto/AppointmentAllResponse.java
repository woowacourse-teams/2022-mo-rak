package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentAllResponse implements Comparable<AppointmentAllResponse> {

    private Long id;

    private String code;

    private String title;

    private String description;

    private Integer durationHours;

    private Integer durationMinutes;

    @JsonProperty("isClosed")
    private Boolean closed;

    private Integer count;

    public static AppointmentAllResponse from(Appointment appointment) {
        return new AppointmentAllResponse(
                appointment.getId(),
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.parseHours(),
                appointment.parseMinutes(),
                appointment.isClosed(),
                appointment.getCount()
        );
    }

    @Override
    public int compareTo(AppointmentAllResponse o) {
        if (this.closed.equals(o.closed)) {
            return Long.compare(o.id, this.id);
        }
        if (Boolean.FALSE.equals(this.closed)) {
            return -1;
        }
        return 1;
    }
}
