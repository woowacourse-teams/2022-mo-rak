package com.morak.back.appointment.domain.menu;

import com.morak.back.core.domain.Code;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Menu {

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "team_code"))
    private Code teamCode;

    private Long hostId;

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "code"))
    private Code code;

    @Embedded
    private Title title;

    @Embedded
    private Description description;

    @Enumerated(value = EnumType.STRING)
    private MenuStatus status;

    @Embedded
    private ClosedAt closedAt;

    public Menu(Code teamCode, Long hostId, Code code, String title, String description, MenuStatus status,
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

    public boolean isBelongedTo(String otherTeamCode) {
        return this.teamCode.isEqualTo(otherTeamCode);
    }

    public boolean isClosed() {
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
