package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class PollItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id", nullable = false, updatable = false)
    private List<PollItem> values;

    @Builder
    public PollItems(List<PollItem> values) {
        this.values = values;
    }

    public void validateCount(PollInfo pollInfo) {
        if (this.values.isEmpty() || pollInfo.isAllowedCountGraterThan(this.values.size())) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ITEM_COUNT_OUT_OF_RANGE_ERROR,
                    "투표 항목의 개수(" + values.size() + ")는 " + pollInfo.getAllowedCount().getValue() + "개 이상여야합니다."
            );
        }
    }

    public void doPoll(Member member, Map<PollItem, String> data) {
        for (PollItem pollItem : values) {
            addOrRemove(pollItem, member, data);
        }
    }

    private void addOrRemove(PollItem pollItem, Member member, Map<PollItem, String> data) {
        if (data.containsKey(pollItem)) {
            pollItem.addSelectMember(member, data.get(pollItem));
            return;
        }
        pollItem.remove(member);
    }

    public int countSelectMembers() {
        return (int) values.stream()
                .map(PollItem::getOnlyMembers)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    public boolean containsAll(Collection<?> items) {
        return this.values.containsAll(items);
    }
}
