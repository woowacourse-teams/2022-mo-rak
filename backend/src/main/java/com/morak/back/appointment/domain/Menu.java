package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Menu {

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @Embedded
    private Code code;

    @Embedded
    private Title title;

    @Embedded
    private Description description;

    @Enumerated(value = EnumType.STRING)
    private MenuStatus status;

    @Embedded
    private ClosedAt closedAt;

    public Menu(Team team, Member host, Code code, String title, String description, MenuStatus status,
                ClosedAt closedAt) {
        this.team = team;
        this.host = host;
        this.code = code;
        this.title = new Title(title);
        this.description = new Description(description);
        this.status = status;
        this.closedAt = closedAt;
    }

    public void close() {
        this.status = MenuStatus.CLOSED;
    }
}
