package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private String code;

    private String title;

    private String description;

    private Integer durationHours;

    private Integer durationMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @JsonProperty("isClosed")
    private Boolean closed;

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
