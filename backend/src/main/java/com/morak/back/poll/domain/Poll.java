package com.morak.back.poll.domain;

import com.morak.back.appointment.domain.menu.Menu;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.BaseEntity;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.poll.exception.PollItemNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Poll extends BaseEntity {

    @Embedded
    private Menu menu;

    @Embedded
    private PollItems pollItems;

    private Boolean anonymous;

    // TODO: 2022/10/21 Menu 를 어디서 생성해줄까?

    @Builder
    public Poll(Menu menu, List<PollItem> pollItems, Boolean anonymous, Integer allowedCount) {
        this(null, menu, PollItems.builder().values(pollItems).allowedCount(new AllowedCount(allowedCount)).build(),
                anonymous);
    }

    private Poll(Long id, Menu menu, PollItems pollItems, Boolean anonymous) {
        pollItems.validateCount();
        this.id = id;
        this.menu = menu;
        this.pollItems = pollItems;
        this.anonymous = anonymous;
    }

    public void doPoll(Member member, Map<PollItem, String> data) {
        validateState();
        validateAllowedCount(data.size());
        validateExistItem(data.keySet());

        pollItems.doPoll(member, data);
    }

    private void validateExistItem(Set<PollItem> selectItems) {
        if (!pollItems.containsAll(selectItems)) {
            throw new PollItemNotFoundException(
                    CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR,
                    id + "번 투표에 " +
                            pollItems.getValues().stream()
                                    .map(pollItem -> pollItem.getId().toString())
                                    .collect(Collectors.joining(", ")) + "번 항목들은 투표할 수 없습니다.");
        }
    }

    private void validateState() {
        if (menu.isClosed()) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ALREADY_CLOSED_ERROR,
                    menu.getCode() + " 코드의 투표는 종료되었습니다."
            );
        }
    }

    private void validateAllowedCount(int itemCount) {
        if (!pollItems.isAllowedCount(itemCount)) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR,
                    this.id + "번 투표에 " + itemCount + "개의 투표 항목을 선택할 수 없습니다."
            );
        }
    }

    public void close(Long memberId) {
        menu.close(memberId);
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
}
