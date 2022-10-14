package com.morak.back.role.domain;

import lombok.Getter;

@Getter
public class Role {

    private RoleNames roleNames;

    public Role(RoleNames roleNames) {
        this.roleNames = roleNames;
    }

    public void updateNames(RoleNames roleNames) {
        this.roleNames = roleNames;
    }
}
