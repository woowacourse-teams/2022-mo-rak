package com.morak.back.role.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleHistoryEvent {

    private final String teamCode;
    private final LocalDateTime dateTime;
    private final Map<Long, String> roleNameByMemberIds;

    public static RoleHistoryEvent from(RoleHistory roleHistory, String teamCode) {
        return new RoleHistoryEvent(
                teamCode,
                roleHistory.getDateTime(),
                roleHistory.getMatchResults().stream()
                        .collect(Collectors.toMap(
                                RoleMatchResult::getMemberId,
                                result -> result.getRoleName().getValue()
                        ))
        );
    }

    public String getRoleName(Long memberId) {
        return roleNameByMemberIds.get(memberId);
    }
}
