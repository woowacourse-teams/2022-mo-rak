package com.morak.back.role.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

@Getter
public class RoleHistory implements Comparable<RoleHistory> {

    private final LocalDateTime dateTime;
    private final Map<RoleName, Long> matchResult;

    public RoleHistory(LocalDateTime dateTime, Map<RoleName, Long> matchResult) {
        this.dateTime = dateTime;
        this.matchResult = matchResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleHistory that = (RoleHistory) o;
        return Objects.equals(getDateTime(), that.getDateTime()) && Objects.equals(getMatchResult(),
                that.getMatchResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateTime(), getMatchResult());
    }

    @Override
    public int compareTo(RoleHistory other) {
        if (this.dateTime.isBefore(other.dateTime)) {
            return -1;
        }
        return 1;
    }
}
