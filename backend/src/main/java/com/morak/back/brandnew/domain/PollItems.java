package com.morak.back.brandnew.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class PollItems {

//    @ElementCollection(targetClass = PollItem.class)
//    @CollectionTable(
//            name = "new_poll_item",
//            joinColumns = @JoinColumn(name = "poll_manager_id")
//    )
    @OneToMany(cascade = CascadeType.ALL)
    private List<NewPollItem> values;

    @Builder
    public PollItems(List<NewPollItem> values) {
        this.values = values;
    }

    public void validateCount(Poll poll) {
        if (values.size() <= 0 || poll.isGreaterThan(values.size())) {
            throw new IllegalArgumentException("선택한 항목 개수가 틀립니다");
        }
    }

    public void addOrRemove(Member member, Map<NewPollItem, String> data) {
        for (NewPollItem pollItem : values) {
            addOrRemove(pollItem, member, data);
        }
    }

    private void addOrRemove(NewPollItem pollItem, Member member, Map<NewPollItem, String> data) {
        if (data.containsKey(pollItem)) {
            pollItem.addSelectMember(member, data.get(pollItem));
            return;
        }
        pollItem.remove(member);
    }

    public int countSelectMembers() {
        return (int) values.stream()
                .map(NewPollItem::getOnlyMembers)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }
}
