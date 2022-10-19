package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import java.util.Objects;
import java.util.Set;
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
@Table(name = "poll_item")
public class PollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Embedded
    private SelectMembers selectMembers;

    public PollItem(Long id) {
        this(id, null, new SelectMembers());
    }

    @Builder
    public PollItem(Long id, String subject) {
        this(id, subject, new SelectMembers());
    }

    private PollItem(Long id, String subject, SelectMembers selectMembers) {
        this.id = id;
        this.subject = subject;
        this.selectMembers = selectMembers;
    }

    public Set<Member> getOnlyMembers() {
        return selectMembers.findMembers();
    }

    public void addSelectMember(Member member, String description) {
        selectMembers.add(member, description);
    }

    public void remove(Member member) {
        selectMembers.remove(member);
    }

    public Integer countSelectMembers() {
        return selectMembers.countSelectMembers();
    }

    public Boolean isSelectedBy(Long memberId) {
        return selectMembers.isSelectedBy(memberId);
    }

    public String getDescriptionFrom(Long memberId) {
        return selectMembers.getDescriptionFrom(memberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PollItem)) {
            return false;
        }
        PollItem that = (PollItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
