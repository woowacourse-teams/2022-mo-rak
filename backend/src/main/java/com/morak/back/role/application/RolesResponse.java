package com.morak.back.role.application;

import com.morak.back.role.domain.RoleHistory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RolesResponse {

    private final List<HistoryResponse> roles;

    public static RolesResponse from(List<RoleHistory> roleHistories) {
        return new RolesResponse(roleHistories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList()));
    }
}
