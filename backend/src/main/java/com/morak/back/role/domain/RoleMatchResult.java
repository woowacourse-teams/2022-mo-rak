package com.morak.back.role.domain;

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
}
