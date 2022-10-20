package com.morak.back.role.domain;

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
}
