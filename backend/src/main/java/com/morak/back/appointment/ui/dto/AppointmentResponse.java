package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
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

    public static AppointmentResponse from(Appointment appointment, Long memberId) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.parseHours(),
                appointment.parseMinutes(),
                appointment.getStartDate(),
                appointment.getEndDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getClosedAt(),
                appointment.isClosed(),
                appointment.isHost(memberId)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppointmentResponse that = (AppointmentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code)
                && Objects.equals(title, that.title) && Objects.equals(description, that.description)
                && Objects.equals(durationHours, that.durationHours) && Objects.equals(durationMinutes,
                that.durationMinutes) && Objects.equals(startDate, that.startDate) && Objects.equals(
                endDate, that.endDate) && Objects.equals(startTime, that.startTime) && Objects.equals(
                endTime, that.endTime) && Objects.equals(closedAt, that.closedAt) && Objects.equals(
                closed, that.closed) && Objects.equals(isHost, that.isHost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, title, description, durationHours, durationMinutes, startDate, endDate, startTime,
                endTime, closedAt, closed, isHost);
    }
}
