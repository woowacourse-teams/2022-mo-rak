package com.morak.back.core.domain.menu;

import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
@Builder
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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus status;

    @Embedded
    private ClosedAt closedAt;

    public Menu(Code teamCode, Long hostId, Code code, Title title, MenuStatus status, ClosedAt closedAt) {
        this.teamCode = teamCode;
        this.hostId = hostId;
        this.code = code;
        this.title = title;
        this.status = status;
        this.closedAt = closedAt;
    }

    public void close(Long memberId) {
        validateHost(memberId);
        status = status.close();
    }

    private void validateHost(Long memberId) {
        if (!hostId.equals(memberId)) {
            throw new AuthorizationException(
                    CustomErrorCode.HOST_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + getCode() + " 코드에 해당하는 도메인의 호스트가 아닙니다."
            );
        }
    }

    public boolean isClosed() {
        return this.status.isClosed();
    }

    public Boolean isHost(Long memberId) {
        return this.hostId.equals(memberId);
    }

    public boolean isBelongedTo(String teamCode) {
        return this.teamCode.isEqualTo(teamCode);
    }

    public String getTeamCode() {
        return this.teamCode.getCode();
    }

    public String getCode() {
        return this.code.getCode();
    }

    public String getTitle() {
        return this.title.getTitle();
    }

    public LocalDateTime getClosedAt() {
        return this.closedAt.getClosedAt();
    }

    public String getStatus() {
        return this.status.name();
    }

}
