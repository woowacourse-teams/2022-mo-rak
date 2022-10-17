package com.morak.back.brandnew.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.domain.BaseEntity;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "new_poll")
public class NewPoll extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PollInfo pollInfo;

    @Embedded
    private PollItems pollItems;

    @Builder
    public NewPoll(PollInfo pollInfo, List<NewPollItem> pollItems) {
        this(null, pollInfo, PollItems.builder().values(pollItems).build());
    }

    protected NewPoll(Long id, PollInfo pollInfo, PollItems pollItems) {
        pollItems.validateCount(pollInfo);
        this.id = id;
        this.pollInfo = pollInfo;
        this.pollItems = pollItems;
    }

    public void doPoll(Member member, Map<NewPollItem, String> data) {
        validateState();
        validateAllowedCount(data.size());
        validateExistItem(data.keySet());

        pollItems.doPoll(member, data);
    }

    private void validateExistItem(Set<NewPollItem> selectItems) {
        if (!pollItems.containsAll(selectItems)) {
            throw new PollAuthorizationException(
                    CustomErrorCode.POLL_ITEM_MISMATCHED_ERROR,
                    id + "번 투표에 " +
                            pollItems.getValues().stream()
                                    .map(pollItem -> pollItem.getId().toString())
                                    .collect(Collectors.joining(", ")) + "번 항목들은 투표할 수 없습니다.");
        }
    }

    private void validateState() {
        if (pollInfo.isClosed()) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ALREADY_CLOSED_ERROR,
                    pollInfo.getCode() + " 코드의 투표는 종료되었습니다."
            );
        }
    }

    private void validateAllowedCount(int itemCount) {
        if (!pollInfo.isAllowedCount(itemCount)) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR,
                    pollInfo.getCode() + "번 투표에 " + itemCount + "개의 투표 항목을 선택할 수 없습니다."
            );
        }
    }

    public int countSelectMembers() {
        return pollItems.countSelectMembers();
    }

    public void close(Long memberId) {
        pollInfo.close(memberId);
    }

    public List<NewPollItem> getPollItems() {
        return this.pollItems.getValues();
    }

    public boolean isBelongedTo(Long teamId) {
        return this.pollInfo.getTeamId().equals(teamId);
    }

    public boolean isHost(final Member member) {
        return getPollInfo().isHost(member.getId());
    }
}
