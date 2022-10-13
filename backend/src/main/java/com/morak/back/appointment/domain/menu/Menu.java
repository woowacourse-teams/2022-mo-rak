package com.morak.back.appointment.domain.menu;

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

    private String teamCode;

    private Long hostId;

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

    public Menu(String teamCode, Long hostId, Code code, String title, String description, MenuStatus status,
                ClosedAt closedAt) {
        this.teamCode = teamCode;
        this.hostId = hostId;
        this.code = code;
        this.title = new Title(title);
        this.description = new Description(description);
        this.status = status;
        this.closedAt = closedAt;
    }

    public void close() {
        this.status = MenuStatus.CLOSED;
    }

    public boolean isHost(Long memberId) {
        return this.hostId.equals(memberId);
    }

    public boolean isTeamEquals(String otherTeamCode) {
        return this.teamCode.equals(otherTeamCode);
    }

    public Boolean isClosed() {
        return this.status.isClosed();
    }

    public String getCode() {
        return this.code.getCode();
    }

    public String getTitle() {
        return this.title.getTitle();
    }

    public String getDescription() {
        return this.description.getDescription();
    }

    public LocalDateTime getClosedAt() {
        return this.closedAt.getClosedAt();
    }
}
