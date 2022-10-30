package com.morak.back.poll.domain;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.core.domain.menu.Menu;
import java.time.LocalDateTime;

public class PollEvent extends MenuEvent {

    public PollEvent(String code, String teamCode, String title, LocalDateTime closedAt, boolean closed) {
        super(code, teamCode, title, closedAt, closed);
    }

    public static PollEvent from(Menu menu) {
        return new PollEvent(
                menu.getCode(),
                menu.getTeamCode(),
                menu.getTitle(),
                menu.getClosedAt(),
                menu.isClosed()
        );
    }

}
