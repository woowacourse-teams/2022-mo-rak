package com.morak.back.auth.domain;

import com.morak.back.poll.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member extends BaseEntity {

    private static final Member ANONYMOUS_MEMBER = new Member(
            0L,
            null,
            "",
            "https://user-images.githubusercontent.com/45311765/179645488-2d8c29c8-f8ed-43e9-9951-b30e82ead5ed.png"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId;

    private String name;

    private String profileUrl;

    public static Member getAnonymous() {
        return ANONYMOUS_MEMBER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.getId()) && Objects.equals(oauthId, member.getOauthId())
                && Objects.equals(name, member.getName()) && Objects.equals(profileUrl, member.getProfileUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oauthId, name, profileUrl);
    }
}

