package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentResponse {

    private final String code;

    private final String title;

    private final String description;

    private final Integer durationHours;

    private final Integer durationMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime endTime;

    @JsonProperty("isClosed")
    private final Boolean closed;

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.parseHours(),
                appointment.parseMinutes(),
                appointment.getStartDate(),
                appointment.getEndDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.isClosed()
        );
    }
}
