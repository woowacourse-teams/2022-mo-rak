package com.morak.back.role.domain;

import com.morak.back.core.support.Generated;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RoleMatchResult {

    @Embedded
    private RoleName roleName;

    private Long memberId;

    public RoleMatchResult(RoleName roleName, Long memberId) {
        this.roleName = roleName;
        this.memberId = memberId;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleMatchResult)) {
            return false;
        }
        RoleMatchResult that = (RoleMatchResult) o;
        return Objects.equals(getRoleName(), that.getRoleName())
                && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(getRoleName(), getMemberId());
    }
}
