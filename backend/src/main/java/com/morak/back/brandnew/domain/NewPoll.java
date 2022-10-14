package com.morak.back.brandnew.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.BaseEntity;
import java.util.List;
import java.util.Map;
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
        validateAllowedCount(data);

        pollItems.doPoll(member, data);
    }

    private void validateState() {
        if (pollInfo.isClosed()) {
            throw new IllegalArgumentException("마감되었습니당!");
        }
    }

    private void validateAllowedCount(Map<NewPollItem, String> data) {
        if (data.size() <= 0 || pollInfo.isGreaterThan(data.size())) {
            throw new IllegalArgumentException("선택한 항목 개수가 틀립니다");
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
}
