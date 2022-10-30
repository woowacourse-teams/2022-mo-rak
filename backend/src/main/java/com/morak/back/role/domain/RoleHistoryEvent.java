package com.morak.back.role.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class RoleHistoryEvent {

    private final String teamCode;
    private final LocalDateTime dateTime;
    private final Map<Long, String> roleNameByMemberIds;

    public RoleHistoryEvent(String teamCode, LocalDateTime dateTime, Map<Long, String> roleNameByMemberIds) {
        this.teamCode = teamCode;
        this.dateTime = dateTime;
        this.roleNameByMemberIds = roleNameByMemberIds;
    }

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

    public String getTeamCode() {
        return teamCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Map<Long, String> getRoleNameByMemberIds() {
        return roleNameByMemberIds;
    }
}
