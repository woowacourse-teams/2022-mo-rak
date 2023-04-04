package com.morak.back.poll.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.poll.exception.PollItemNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class PollItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id", nullable = false, updatable = false)
    private List<PollItem> values = new ArrayList<>();

    @Embedded
    private AllowedCount allowedCount;

    @Column(nullable = false)
    private int selectedCount;

    @Transient
    private boolean first = false;

    @Builder
    public PollItems(List<PollItem> values, AllowedCount allowedCount) {
        validateCountAllowed(values, allowedCount);
        this.values = new ArrayList<>(values);
        this.allowedCount = allowedCount;
    }

    private void validateCountAllowed(List<PollItem> values, AllowedCount allowedCount) {
        if (values.isEmpty() || allowedCount.isGreaterThan(values.size())) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_ITEM_COUNT_OUT_OF_RANGE_ERROR,
                    "투표 항목의 개수(" + values.size() + ")는 " + allowedCount.getValue() + "개 이상여야합니다."
            );
        }
    }

    public void doPoll(Long memberId, Map<Long, String> data) {
        validateExistItem(data.keySet());
        validatePollCountAllowed(data.size());
        checkFirst(memberId);

        for (PollItem pollItem : values) {
            addOrRemove(pollItem, memberId, data);
        }
    }

    private void checkFirst(Long memberId) {
        if (isFirstPoll(memberId)) {
            this.first = true;
        }
    }

    private void validateExistItem(Set<Long> selectItems) {
        List<Long> pollItemIds = values.stream()
                .map(PollItem::getId)
                .collect(Collectors.toList());
        if (!new HashSet<>(pollItemIds).containsAll(selectItems)) {
            throw new PollItemNotFoundException(
                    CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR,
                    values.stream()
                            .map(pollItem -> pollItem.getId().toString())
                            .collect(Collectors.joining(", ")) + "번 항목들은 투표할 수 없습니다.");
        }
    }

    private void validatePollCountAllowed(int itemCount) {
        if (itemCount < 1 || this.allowedCount.isLessThan(itemCount)) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR,
                    allowedCount.getValue() + "개 보다 많은(" + itemCount + ") 투표 항목을 선택할 수 없습니다."
            );
        }
    }

    private void addOrRemove(PollItem pollItem, Long memberId, Map<Long, String> data) {
        if (data.containsKey(pollItem.getId())) {
            pollItem.addSelectMember(memberId, data.get(pollItem.getId()));
            return;
        }
        pollItem.remove(memberId);
    }

    private boolean isFirstPoll(Long memberId) {
        return this.values.stream()
                .map(PollItem::getOnlyMembers)
                .flatMap(Collection::stream)
                .noneMatch(memberId::equals);
    }
}
