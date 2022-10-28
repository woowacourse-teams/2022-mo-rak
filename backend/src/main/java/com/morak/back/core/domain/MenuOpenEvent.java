package com.morak.back.core.domain;

import com.morak.back.appointment.domain.AppointmentOpenEvent;
import com.morak.back.core.domain.menu.Menu;

public class MenuOpenEvent {

    private final String code;
    private final String teamCode;

    public MenuOpenEvent(String code, String teamCode) {
        this.code = code;
        this.teamCode = teamCode;
    }

    public static AppointmentOpenEvent from(Menu menu) {
        return new AppointmentOpenEvent(menu.getCode(), menu.getTeamCode());
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getCode() {
        return code;
    }
}
