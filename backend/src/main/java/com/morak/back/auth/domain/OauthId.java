package com.morak.back.auth.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class OauthId {

    @Size(min = 1, max = 255, message = "oauthId의 길이는 1 ~ 255 사이여야합니다.")
    private String oauthId;

    public OauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OauthId oauthId = (OauthId) o;
        return Objects.equals(this.oauthId, oauthId.oauthId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oauthId);
    }
}
