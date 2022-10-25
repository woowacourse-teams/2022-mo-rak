package com.morak.back.auth.domain;

import com.morak.back.core.exception.CustomErrorCode;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class OAuthId {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    @Column(name = "oauth_id", nullable = false)
    private String value;

    public OAuthId(String value) {
        validateBlank(value);
        validateLength(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new MemberDomainLogicException(CustomErrorCode.OAUTH_ID_BLANK_ERROR,
                    "OAuthId는 `" + value + "`일 수 없습니다");
        }
    }

    private void validateLength(String value) {
        int length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new MemberDomainLogicException(
                    CustomErrorCode.OAUTH_ID_LENGTH_ERROR,
                    "OAuthId의 길이 " + length + "는 " + MIN_LENGTH + "이상, " + MAX_LENGTH + "이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        OAuthId oAuthId = (OAuthId) o;
        return Objects.equals(value, oAuthId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
