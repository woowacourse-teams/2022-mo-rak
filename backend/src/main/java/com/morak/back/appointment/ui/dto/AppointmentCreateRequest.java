package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AppointmentCreateRequest {

    @NotBlank
    private final String title;

    // TODO: 2022/07/27 프론트와 상의 후 길이 제한 두기
    @NotNull
    private final String description;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime startTime;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime endTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 24)
    private final Integer durationHour;

    @NotNull
    @Min(value = 0)
    @Max(value = 59)
    private final Integer durationMinute;

    @JsonCreator
    public AppointmentCreateRequest(String title, String description, LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime, Integer durationHour,
                                    Integer durationMinute) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationHour = durationHour;
        this.durationMinute = durationMinute;
    }
}
