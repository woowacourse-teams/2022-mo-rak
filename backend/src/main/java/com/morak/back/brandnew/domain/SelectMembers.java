package com.morak.back.brandnew.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import lombok.Builder;
import lombok.Getter;

@Embeddable
//@NoArgsConstructor
@Getter
public class SelectMembers {

    @ElementCollection
    @CollectionTable(
            name = "select_member",
            joinColumns = @JoinColumn(name = "new_poll_item_id")
    )
    @MapKeyColumn
    @Column(name = "description")
    private Map<Member, String> values;

    public SelectMembers() {
        this(new HashMap<>());
    }

    @Builder
    public SelectMembers(Map<Member, String> values) {
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
}
