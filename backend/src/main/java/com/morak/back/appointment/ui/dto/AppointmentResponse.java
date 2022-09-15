package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.appointment.Appointment;
import com.morak.back.auth.domain.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;

    private String code;

    private String title;

    private String description;

    private Integer durationHours;

    private Integer durationMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma", locale = "en_US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma", locale = "en_US")
    private LocalTime endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    @JsonProperty("isClosed")
    private Boolean closed;

    private Boolean isHost;

    public static AppointmentResponse from(Appointment appointment, Member member) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.parseDurationHours(),
                appointment.parseDurationMinutes(),
                appointment.getStartDate(),
                appointment.getEndDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getClosedAt(),
                appointment.isClosed(),
                appointment.isHost(member)
        );
    }
}
