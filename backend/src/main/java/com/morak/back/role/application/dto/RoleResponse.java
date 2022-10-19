package com.morak.back.role.application.dto;

import com.morak.back.role.domain.RoleMatchResult;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long memberId;
    private String name;

    public static RoleResponse from(Entry<String, Long> matchResult) {
        return new RoleResponse(matchResult.getValue(), matchResult.getKey());
    }

    public static RoleResponse from(RoleMatchResult matchResult) {
        return new RoleResponse(matchResult.getMemberId(), matchResult.getRoleName().getValue());
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
