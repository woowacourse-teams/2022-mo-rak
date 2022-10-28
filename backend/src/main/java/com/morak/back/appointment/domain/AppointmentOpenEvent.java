package com.morak.back.appointment.domain;

import com.morak.back.core.domain.MenuOpenEvent;
import com.morak.back.core.domain.menu.Menu;

public class AppointmentOpenEvent extends MenuOpenEvent {

    public AppointmentOpenEvent(String code, String teamCode) {
        super(code, teamCode);
    }

    public static AppointmentOpenEvent from(Menu menu) {
        return new AppointmentOpenEvent(menu.getCode(), menu.getTeamCode());
    }
}
