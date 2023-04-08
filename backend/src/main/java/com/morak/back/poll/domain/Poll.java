package com.morak.back.poll.domain;

import com.morak.back.core.domain.BaseRootEntity;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.domain.menu.Menu;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.core.domain.menu.Title;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Poll extends BaseRootEntity<Poll> {

    @Embedded
    private Menu menu;

    @Embedded
    private PollItems pollItems;

    @Column(nullable = false)
    private boolean anonymous;

    @Builder
    public Poll(Code teamCode, Long hostId, Code code, String title, MenuStatus status, ClosedAt closedAt,
                List<PollItem> pollItems, int allowedCount, boolean anonymous) {
        this(null,
                Menu.builder()
                        .teamCode(teamCode)
                        .hostId(hostId)
                        .code(code)
                        .title(new Title(title))
                        .status(status)
                        .closedAt(closedAt)
                        .build(),
                PollItems.builder()
                        .values(pollItems)
                        .allowedCount(new AllowedCount(allowedCount))
                        .build(),
                anonymous
        );
    }

    private Poll(Long id, Menu menu, PollItems pollItems, boolean anonymous) {
        super(id);
        this.menu = menu;
        this.pollItems = pollItems;
        this.anonymous = anonymous;
        registerEvent(PollEvent.from(menu));
    }

    public void doPoll(Long memberId, Map<Long, String> data) {
        validateStatusOpen();

        pollItems.doPoll(memberId, data);
    }

    private void validateStatusOpen() {
        if (menu.isClosed()) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ALREADY_CLOSED_ERROR,
                    menu.getCode() + " 코드의 투표는 종료되었습니다."
            );
        }
    }

    public void close(Long memberId) {
        menu.close(memberId);
        registerEvent(PollEvent.from(menu));
    }

    public List<PollItem> getPollItems() {
        return this.pollItems.getValues();
    }

    public AllowedCount getAllowedCount() {
        return this.pollItems.getAllowedCount();
    }

    public boolean isBelongedTo(String teamCode) {
        return this.menu.isBelongedTo(teamCode);
    }

    public boolean isHost(final Long memberId) {
        return this.menu.isHost(memberId);
    }

    public String getCode() {
        return menu.getCode();
    }

    public String getTeamCode() {
        return menu.getTeamCode();
    }

    public Long getHostId() {
        return menu.getHostId();
    }

    public String getTitle() {
        return menu.getTitle();
    }

    public LocalDateTime getClosedAt() {
        return this.menu.getClosedAt();
    }

    public boolean isClosed() {
        return this.menu.isClosed();
    }

    public String getStatus() {
        return this.menu.getStatus();
    }

    public int getSelectedCount() {
        return this.pollItems.getSelectedCount();
    }

}
