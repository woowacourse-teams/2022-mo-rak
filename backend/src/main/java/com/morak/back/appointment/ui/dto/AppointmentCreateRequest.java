package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.appointment.domain.Appointment;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.team.domain.Team;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AppointmentCreateRequest {

    @NotBlank
    private final String title;

    @NotNull
    private final String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma", locale = "en_US")
    private final LocalTime startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma", locale = "en_US")
    private final LocalTime endTime;

    @NotNull
    private final Integer durationHours;

    @NotNull
    private final Integer durationMinutes;

    @JsonCreator
    public AppointmentCreateRequest(String title, String description, LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime, Integer durationHours,
                                    Integer durationMinutes) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
    }

    public Appointment toAppointment(Team team, Member member, Code code) {
        return Appointment.builder()
                .team(team)
                .host(member)
                .title(this.title)
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .durationHours(this.durationHours)
                .durationMinutes(this.durationMinutes)
                .code(code)
                .build();
    }
}
