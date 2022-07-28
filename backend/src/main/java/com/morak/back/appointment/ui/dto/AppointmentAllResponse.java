package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentAllResponse {

    private final String code;

    private final String title;

    private final String description;

    private final Integer durationHours;

    private final Integer durationMinutes;

    @JsonProperty("isClosed")
    private final Boolean closed;

    private final Integer count;

    public static AppointmentAllResponse from(Appointment appointment) {
        return new AppointmentAllResponse(
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.parseHours(),
                appointment.parseMinutes(),
                appointment.isClosed(),
                appointment.getCount()
        );
    }
}
