package com.morak.back.role.application.dto;

import com.morak.back.role.domain.RoleName;
import com.morak.back.role.domain.RoleNames;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleNameResponses {

    private List<String> roles;

    public static RoleNameResponses from(RoleNames roleNames) {
        return new RoleNameResponses(roleNames.getValues().stream()
                .map(RoleName::getValue)
                .collect(Collectors.toList()));
    }
}
