package com.morak.back.role.application.dto;

import com.morak.back.role.domain.RoleHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {

    private LocalDate date;
    private List<RoleResponse> role;

    public static HistoryResponse from(RoleHistory roleHistory) {
        return new HistoryResponse(
                roleHistory.getDateTime().toLocalDate(),
                toRoleResponses(roleHistory)
        );
    }

    private static List<RoleResponse> toRoleResponses(RoleHistory roleHistory) {
        return roleHistory.getMatchResults()
                .stream()
                .map(RoleResponse::from)
                .collect(Collectors.toList());
    }
}
