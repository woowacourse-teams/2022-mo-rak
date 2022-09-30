package com.morak.back.brandnew;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Builder;

public class SelectMembers {

    private final Map<Member, String> values;

    public SelectMembers() {
        this(new HashMap<>());
    }

    @Builder
    public SelectMembers(Map<Member, String> values) {
        this.values = values;
    }

    public Set<Member> getMembers() {
        return values.keySet();
    }

    public void add(Member member, String description) {
        values.put(member, description);
    }

    public void remove(Member member) {
        values.remove(member);
    }
}
