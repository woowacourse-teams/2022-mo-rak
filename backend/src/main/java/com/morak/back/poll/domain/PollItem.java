package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Subject subject;

    @ElementCollection
    @CollectionTable(
            name = "select_member",
            joinColumns = @JoinColumn(name = "poll_item_id")
    )
    @MapKeyJoinColumn(name = "member_id")
    @Column(name = "description", nullable = false)
    private Map<Member, String> selectMembers = new HashMap<>();

    public PollItem(Long id) {
        this(id, null, new HashMap<>());
    }

    @Builder
    public PollItem(Long id, String subject) {
        this(id, new Subject(subject), new HashMap<>());
    }

    private PollItem(Long id, Subject subject, Map<Member, String> selectMembers) {
        this.id = id;
        this.subject = subject;
        this.selectMembers = selectMembers;
    }

    public Set<Member> getOnlyMembers() {
        return selectMembers.keySet();
    }

    public void addSelectMember(Member member, String description) {
        selectMembers.put(member, description);
    }

    public void remove(Member member) {
        selectMembers.remove(member);
    }

    public Integer countSelectMembers() {
        return selectMembers.size();
    }

    public Boolean isSelectedBy(Long memberId) {
        return selectMembers.keySet().stream()
                .anyMatch(member -> member.isSameId(memberId));
    }

    public String getDescriptionFrom(Long memberId) {
        return selectMembers.entrySet().stream()
                .filter(entry -> entry.getKey().isSameId(memberId))
                .map(Entry::getValue)
                .findFirst()
                .orElse("");
    }

    public String getSubject() {
        return this.subject.getValue();
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
