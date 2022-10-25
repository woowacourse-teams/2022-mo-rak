package com.morak.back.auth.domain;

import com.morak.back.core.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Member extends BaseEntity {

    @Transient
    private static final Member ANONYMOUS_MEMBER = new Member(
            0L,
            "00000000",
            "anonymous",
            "https://user-images.githubusercontent.com/45311765/179645488-2d8c29c8-f8ed-43e9-9951-b30e82ead5ed.png"
    );

    @Embedded
    private OAuthId oauthId;

    @Embedded
    private Name name;

    @Embedded
    private ProfileUrl profileUrl;

    @Builder
    public Member(Long id, String oauthId, String name, String profileUrl) {
        super(id);
        this.oauthId = new OAuthId(oauthId);
        this.name = new Name(name);
        this.profileUrl = new ProfileUrl(profileUrl);
    }

    public static Member getAnonymousMember() {
        return ANONYMOUS_MEMBER;
    }

    public void changeName(String name) {
        this.name = new Name(name);
    }

    public String getOauthId() {
        return this.oauthId.getValue();
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getProfileUrl() {
        return this.profileUrl.getValue();
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
        return Objects.equals(oauthId, member.oauthId) && Objects.equals(name, member.name)
                && Objects.equals(profileUrl, member.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oauthId, name, profileUrl);
    }

    public boolean isSameId(Long memberId) {
        return this.id.equals(memberId);
    }
}

