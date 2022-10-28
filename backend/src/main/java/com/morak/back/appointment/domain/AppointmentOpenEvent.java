package com.morak.back.appointment.domain;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.core.domain.menu.Menu;

public class AppointmentOpenEvent extends MenuEvent {

    public AppointmentOpenEvent(String code, String teamCode) {
        super(code, teamCode);
    }

    public static AppointmentOpenEvent from(Menu menu) {
        return new AppointmentOpenEvent(menu.getCode(), menu.getTeamCode());
    }
}
