package com.morak.back.notification.domain.slack;

import com.morak.back.core.domain.menu.Menu;

public class MenuCreateEvent {

    private final String code;

    public MenuCreateEvent(String code) {
        this.code = code;
    }

    public static MenuCreateEvent from(Menu menu) {
        return new MenuCreateEvent(menu.getCode());
    }

    public String getCode() {
        return code;
    }
}
