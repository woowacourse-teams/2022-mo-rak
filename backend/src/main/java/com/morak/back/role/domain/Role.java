package com.morak.back.role.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class Role {

    private static final String DEFAULT_ROLE = "데일리 마스터";

    private RoleNames roleNames;
    private final RoleHistories roleHistories;

    private Role(RoleNames roleNames, RoleHistories roleHistories) {
        this.roleNames = roleNames;
        this.roleHistories = roleHistories;
    }

    public Role() {
        this(RoleNames.from(List.of(DEFAULT_ROLE)), new RoleHistories());
    }

    public void updateNames(List<String> names) {
        this.roleNames = RoleNames.from(names);
    }

    public RoleHistory matchMembers(List<Long> memberIds, ShuffleStrategy strategy) {
        strategy.shuffle(memberIds);
        RoleHistory roleHistory = new RoleHistory(LocalDateTime.now(), roleNames.match(memberIds));
        roleHistories.add(roleHistory);
        return roleHistory;
    }
}
