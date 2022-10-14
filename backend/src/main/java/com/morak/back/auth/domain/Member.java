package com.morak.back.auth.domain;

import com.morak.back.core.support.Generated;
import com.morak.back.poll.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member extends BaseEntity {

    @Transient
    private static final Member ANONYMOUS_MEMBER = new Member(
            0L,
            "00000000",
            "anonymous",
            "https://user-images.githubusercontent.com/45311765/179645488-2d8c29c8-f8ed-43e9-9951-b30e82ead5ed.png"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "oauthId 는 blank 일 수 없습니다.")
    @Size(min = 1, max = 255, message = "oauthId의 길이는 1 ~ 255 사이여야합니다.")
    private String oauthId;

    @NotBlank(message = "name 은 blank 일 수 없습니다.")
    @Size(min = 1, max = 255, message = "name의 길이는 1 ~ 255 사이여야합니다.")
    private String name;

    @NotBlank(message = "profileUrl은 blank 일 수 없습니다.")
    @Size(min = 1, max = 255, message = "profileUrl의 길이는 1 ~ 255 사이여야합니다.")
    @URL(regexp = "^(http).*", message = "profileUrl은 http로 시작해야 합니다.")
    private String profileUrl;

    public static Member getAnonymous() {
        return ANONYMOUS_MEMBER;
    }

    @Override
    @Generated
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
    @Generated
    public int hashCode() {
        return Objects.hash(id, oauthId, name, profileUrl);
    }

    public boolean isSameId(Long memberId) {
        return this.id.equals(memberId);
    }
}

