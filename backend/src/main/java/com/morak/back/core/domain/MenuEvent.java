package com.morak.back.core.domain;

public class MenuEvent {

    private final String code;
    private final String teamCode;

    public MenuEvent(String code, String teamCode) {
        this.code = code;
        this.teamCode = teamCode;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getCode() {
        return code;
    }
}
