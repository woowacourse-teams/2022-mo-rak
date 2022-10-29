package com.morak.back.core.domain;

import java.time.LocalDateTime;

public class MenuEvent {

    private final String code;
    private final String teamCode;
    private final String title;
    private final LocalDateTime closedAt;
    private final boolean closed;

    public MenuEvent(String code, String teamCode, String title, LocalDateTime closedAt, boolean closed) {
        this.code = code;
        this.teamCode = teamCode;
        this.title = title;
        this.closedAt = closedAt;
        this.closed = closed;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public boolean isClosed() {
        return closed;
    }
}
