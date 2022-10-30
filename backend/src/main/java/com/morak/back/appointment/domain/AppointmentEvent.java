package com.morak.back.appointment.domain;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.core.domain.menu.Menu;
import java.time.LocalDateTime;

public class AppointmentEvent extends MenuEvent {

    public AppointmentEvent(String code, String teamCode, String title, LocalDateTime closedAt, boolean isClosed) {
        super(code, teamCode, title, closedAt, isClosed);
    }

    public static AppointmentEvent from(Menu menu) {
        return new AppointmentEvent(
                menu.getCode(),
                menu.getTeamCode(),
                menu.getTitle(),
                menu.getClosedAt(),
                menu.isClosed()
        );
    }
}
