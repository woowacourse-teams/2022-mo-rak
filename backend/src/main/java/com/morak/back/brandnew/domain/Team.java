package com.morak.back.brandnew.domain;

import java.util.List;
import lombok.Builder;

public class Team {

    private final String name;
    private final List<Member> members;

    @Builder
    private Team(String name, List<Member> members) {
        this.name = name;
        this.members = members;
    }

    public boolean contains(Member member) {
        return members.contains(member);
    }
}
