package com.morak.back.brandnew;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PollManager {

    private final Team team;
    private final Poll poll;
    private final List<PollItem> pollItems;

    @Builder
    public PollManager(Team team, Poll poll, List<PollItem> pollItems) {
        this.team = team;
        this.poll = poll;
        this.pollItems = pollItems;
    }

    public void select(Member member, Map<PollItem, String> data) {
        if (poll.isClosed()) {
            throw new IllegalArgumentException("마감되었습니당!");
        }

        if (!team.contains(member)) {
            throw new IllegalArgumentException("팀에 속하지 않는 멤버임");
        }

        if (data.size() <= 0 || poll.isGreaterThan(data.size())) {
            throw new IllegalArgumentException("선택한 항목 개수가 틀립니다");
        }

        for (PollItem pollItem : pollItems) {
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
        return (int) pollItems.stream()
                .map(PollItem::getOnlyMembers)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    public void close(Member member) {
        poll.close(member);
    }
}
