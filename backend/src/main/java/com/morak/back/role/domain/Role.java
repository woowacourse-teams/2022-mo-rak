package com.morak.back.role.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Role {

    private static final String DEFAULT_ROLE = "데일리 마스터";

    private RoleNames roleNames;

    private Role(RoleNames roleNames) {
        this.roleNames = roleNames;
    }
    public Role() {
        this(RoleNames.from(List.of(DEFAULT_ROLE)));
    }

    public void updateNames(List<String> names) {
        this.roleNames = RoleNames.from(names);
    }

    public Map<RoleName, Long> matchMembers(List<Long> memberIds, ShuffleStrategy strategy) {
        strategy.shuffle(memberIds);
        return roleNames.match(memberIds);
    }
}
