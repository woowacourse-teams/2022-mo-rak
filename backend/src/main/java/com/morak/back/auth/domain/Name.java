package com.morak.back.auth.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Name {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    @Column(name = "name", nullable = false)
    private String value;

    public Name(String value) {
        validateBlank(value);
        validateLength(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new MemberDomainLogicException(CustomErrorCode.MEMBER_NAME_BLANK_ERROR, "멤버 이름은 공백일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        int length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new MemberDomainLogicException(
                    CustomErrorCode.OAUTH_ID_LENGTH_ERROR,
                    "멤버 이름 " + value + "는 길이가" + MIN_LENGTH + "이상, " + MAX_LENGTH + "이하여야 합니다.");
        }
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
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
