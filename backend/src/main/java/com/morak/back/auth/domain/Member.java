package com.morak.back.auth.domain;

import com.morak.back.poll.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member extends BaseEntity {

    private static final Member ANONYMOUS_MEMBER = new Member(
            0L,
            "00000000",
            "anonymous",
            "https://user-images.githubusercontent.com/45311765/179645488-2d8c29c8-f8ed-43e9-9951-b30e82ead5ed.png"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Valid
    private OauthId oauthId;

    @Embedded
    @Valid
    private Name name;

    @Embedded
    @Valid
    private ProfileUrl profileUrl;

    @Builder
    public Member(Long id, String oauthId, String name, String profileUrl) {
        this.id = id;
        this.oauthId = new OauthId(oauthId);
        this.name = new Name(name);
        this.profileUrl = new ProfileUrl(profileUrl);
    }

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
        return Objects.equals(id, member.getId()) && Objects.equals(oauthId, member.oauthId)
                && Objects.equals(name, member.name) && Objects.equals(profileUrl, member.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oauthId, name, profileUrl);
    }

    public String getOauthId() {
        return oauthId.getOauthId();
    }

    public String getName() {
        return name.getName();
    }

    public String getProfileUrl() {
        return profileUrl.getProfileUrl();
    }
}

