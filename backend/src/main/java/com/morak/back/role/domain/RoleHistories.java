package com.morak.back.role.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RoleHistories {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "role_id", nullable = false, updatable = false)
    private List<RoleHistory> values = new ArrayList<>();

    private RoleHistories(List<RoleHistory> values) {
        this.values = values;
    }

    public void add(RoleHistory roleHistory) {
        this.values.add(roleHistory);
    }

    public List<RoleHistory> findAllGroupByDate() {
        return groupByDate().values().stream()
                .map(this::extractLatest)
                .collect(Collectors.toList());
    }

    private Map<LocalDate, List<RoleHistory>> groupByDate() {
        return this.values.stream()
                .collect(Collectors.groupingBy(roleHistory -> roleHistory.getDateTime().toLocalDate()));
    }

    private RoleHistory extractLatest(List<RoleHistory> histories) {
        return histories.stream()
                .max(RoleHistory::compareTo)
                .orElseThrow();
    }
}
