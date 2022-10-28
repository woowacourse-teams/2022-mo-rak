package com.morak.back.appointment.domain;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.core.domain.menu.Menu;

public class AppointmentClosedEvent extends MenuEvent {

    public AppointmentClosedEvent(String code, String teamCode) {
        super(code, teamCode);
    }

    public static AppointmentClosedEvent from(Menu menu) {
        return new AppointmentClosedEvent(menu.getCode(), menu.getTeamCode());
    }
}
