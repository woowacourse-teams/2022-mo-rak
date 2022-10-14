package com.morak.back.role.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Embeddable
@Getter
public class RoleName {

    private static final int MAX_LENGTH = 20;

    @Column(name = "role_name", nullable = false)
    private String value;

    public RoleName(String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new RoleDomainLogicException(
                    CustomErrorCode.ROLE_NAME_LENGTH_ERROR,
                    value + "는 " + MAX_LENGTH + "자를 넘을 수 없습니다."
            );
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
        RoleName roleName = (RoleName) o;
        return Objects.equals(value, roleName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RoleName{" +
                "value='" + value + '\'' +
                '}';
    }
}
