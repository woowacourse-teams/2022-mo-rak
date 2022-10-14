package com.morak.back.role.application.dto;

import com.morak.back.role.domain.RoleHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        Map<String, Long> matchResult = getMatchResult(roleHistory);

        return new HistoryResponse(
                roleHistory.getDateTime().toLocalDate(),
                matchResult.entrySet().stream()
                        .map(RoleResponse::from)
                        .collect(Collectors.toList())
        );
    }

    private static Map<String, Long> getMatchResult(RoleHistory roleHistory) {
        return roleHistory.getMatchResult()
                .entrySet().stream()
                .collect(Collectors.toMap(
                        it -> it.getKey().getValue(), Entry::getValue
                ));
    }
}
