package com.morak.back.brandnew.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity(name = "newMember")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    private final String name;

    protected Member() {
        this(null, null);
    }

    @Builder
    public Member(Long id, String name) {
        this.id = id;
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
        if (o == null || !(o instanceof Member)) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(getName(), member.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
