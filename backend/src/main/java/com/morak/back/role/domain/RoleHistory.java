package com.morak.back.role.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RoleHistory implements Comparable<RoleHistory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @ElementCollection
    @CollectionTable(name = "role_match_result", joinColumns = @JoinColumn(name = "role_history_id"))
    @MapKeyColumn(name = "role_name")
    @Column(name = "member_id", nullable = false)
    private Map<RoleName, Long> matchResult;

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
