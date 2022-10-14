package com.morak.back.role.application;

import java.util.Map.Entry;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleResponse {

    private final Long memberId;
    private final String name;

    public static RoleResponse from(Entry<String, Long> matchResult) {
        return new RoleResponse(matchResult.getValue(), matchResult.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleResponse that = (RoleResponse) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, name);
    }
}
