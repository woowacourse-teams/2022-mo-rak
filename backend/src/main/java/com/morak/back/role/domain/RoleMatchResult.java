package com.morak.back.role.domain;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RoleMatchResult {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "role_name"))
    private RoleName roleName;

    private Long memberId;

    public RoleMatchResult(RoleName roleName, Long memberId) {
        this.roleName = roleName;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleMatchResult that = (RoleMatchResult) o;
        return Objects.equals(roleName, that.roleName) && Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, memberId);
    }
}
