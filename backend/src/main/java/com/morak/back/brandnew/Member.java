package com.morak.back.brandnew;

import java.util.Objects;
import lombok.Builder;

public class Member {

    private final String name;

    @Builder
    public Member(String name) {
        this.name = name;
    }

    public boolean isSame(Member member) {
        return this.equals(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(name, member.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
