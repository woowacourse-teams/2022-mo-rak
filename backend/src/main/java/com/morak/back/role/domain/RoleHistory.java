package com.morak.back.role.domain;

import com.morak.back.core.support.Generated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @CollectionTable(
            name = "role_match_result",
            joinColumns = @JoinColumn(name = "role_history_id")
    )
    private Set<RoleMatchResult> matchResults = new LinkedHashSet<>();

    public RoleHistory(LocalDateTime dateTime, List<RoleMatchResult> matchResults) {
        this.dateTime = dateTime;
        this.matchResults = new LinkedHashSet<>(matchResults);
    }

    public List<RoleMatchResult> getMatchResults() {
        return new ArrayList<>(matchResults);
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleHistory that = (RoleHistory) o;
        return Objects.equals(getDateTime(), that.getDateTime())
                && Objects.equals(getMatchResults(), that.getMatchResults());
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(getDateTime(), getMatchResults());
    }

    @Override
    public int compareTo(RoleHistory other) {
        if (this.dateTime.isBefore(other.dateTime)) {
            return -1;
        }
        return 1;
    }
}
