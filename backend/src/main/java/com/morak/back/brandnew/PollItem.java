package com.morak.back.brandnew;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PollItem {

    private final String subject;
    private final SelectMembers selectMembers;

    @Builder
    public PollItem(String subject) {
        this(subject, new SelectMembers());
    }

    private PollItem(String subject, SelectMembers selectMembers) {
        this.subject = subject;
        this.selectMembers = selectMembers;
    }

    public Set<Member> getOnlyMembers() {
        return selectMembers.getMembers();
    }

    public void addSelectMember(Member member, String description) {
        selectMembers.add(member, description);
    }

    public void remove(Member member) {
        selectMembers.remove(member);
    }
}
