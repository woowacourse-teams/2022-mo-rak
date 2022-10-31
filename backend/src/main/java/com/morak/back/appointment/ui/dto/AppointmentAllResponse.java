package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.Appointment;
import java.time.LocalDateTime;
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

    private String subTitle;

    private Integer durationHours;

    private Integer durationMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    @JsonProperty("isClosed")
    private Boolean closed;

    private long count;

    public static AppointmentAllResponse from(Appointment appointment) {
        return new AppointmentAllResponse(
                appointment.getId(),
                appointment.getCode(),
                appointment.getTitle(),
                appointment.getSubTitle(),
                appointment.parseHours(),
                appointment.parseMinutes(),
                appointment.getClosedAt(),
                appointment.isClosed(),
                appointment.getSelected()
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
