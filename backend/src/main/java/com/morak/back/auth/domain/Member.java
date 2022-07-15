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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId;

    private String name;

    private String profileUrl;

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

