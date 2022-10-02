package com.morak.back.brandnew.domain;

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
@Table(name = "new_poll_item")
public class NewPollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Embedded
    private SelectMembers selectMembers;

    public NewPollItem(Long id) {
        this(id, null, new SelectMembers());
    }

    @Builder
    public NewPollItem(String subject) {
        this(null, subject, new SelectMembers());
    }

    private NewPollItem(Long id, String subject, SelectMembers selectMembers) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof NewPollItem)) {
            return false;
        }
        NewPollItem that = (NewPollItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
