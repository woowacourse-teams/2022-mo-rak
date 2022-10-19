package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Embeddable
public class SelectMembers {

    @ElementCollection
    @CollectionTable(
            name = "select_member",
            joinColumns = @JoinColumn(name = "poll_item_id")
    )
    @MapKeyJoinColumn(name = "member_id")
    @Column(name = "description", nullable = false)
    private Map<Member, String> values;

    @Builder
    public SelectMembers() {
        this(new HashMap<>());
    }

    protected SelectMembers(Map<Member, String> values) {
        this.values = values;
    }

    public Set<Member> findMembers() {
        return values.keySet();
    }

    public void add(Member member, String description) {
        values.put(member, description);
    }

    public void remove(Member member) {
        values.remove(member);
    }

    public Integer countSelectMembers() {
        return values.size();
    }

    public Boolean isSelectedBy(Long memberId) {
        return values.keySet().stream()
                .anyMatch(member -> member.isSameId(memberId));
    }

    public String getDescriptionFrom(Long memberId) {
        return values.entrySet().stream()
                .filter(entry -> entry.getKey().isSameId(memberId))
                .map(Entry::getValue)
                .findFirst()
                .orElse("");
    }
}
