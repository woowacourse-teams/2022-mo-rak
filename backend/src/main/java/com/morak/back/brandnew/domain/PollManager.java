package com.morak.back.brandnew.domain;

import java.util.List;
import java.util.Map;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PollManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Poll poll;

    @Embedded
    private PollItems pollItems;

    @Builder
    public PollManager(Poll poll, List<NewPollItem> pollItems) {
        this(null, poll, PollItems.builder().values(pollItems).build());
    }

    @Builder
    public PollManager(Long id, Poll poll, PollItems pollItems) {
        pollItems.validateCount(poll);
        this.id = id;
        this.poll = poll;
        this.pollItems = pollItems;
    }

    public void select(Member member, Map<NewPollItem, String> data) {
        validateState();
        validateAllowedCount(data);

        pollItems.addOrRemove(member, data);
    }

    private void validateState() {
        if (poll.getClosed()) {
            throw new IllegalArgumentException("마감되었습니당!");
        }
    }

    private void validateAllowedCount(Map<NewPollItem, String> data) {
        if (data.size() <= 0 || poll.isGreaterThan(data.size())) {
            throw new IllegalArgumentException("선택한 항목 개수가 틀립니다");
        }
    }

    public int countSelectMembers() {
        return pollItems.countSelectMembers();
    }

    public void close(Member member) {
        poll.close(member);
    }
}
