package com.morak.back.auth.domain;

import com.morak.back.core.exception.InappropriateLengthException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Embeddable
@NoArgsConstructor
@Getter
public class OauthId {

    private String oauthId;

    public OauthId(String oauthId) {
        validate(oauthId);
        this.oauthId = oauthId;
    }

    private void validate(String oauthId) {
        if (ObjectUtils.isEmpty(oauthId)) {
            throw InappropriateLengthException.of("OauthId", oauthId);
        }
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
