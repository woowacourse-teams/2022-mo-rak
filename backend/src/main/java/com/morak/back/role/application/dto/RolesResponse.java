package com.morak.back.role.application.dto;

import com.morak.back.role.domain.RoleHistory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolesResponse {

    private List<HistoryResponse> roles;

    public static RolesResponse from(List<RoleHistory> roleHistories) {
        return new RolesResponse(roleHistories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList()));
    }
}
