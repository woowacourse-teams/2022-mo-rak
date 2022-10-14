package com.morak.back.role.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoleHistories {

    private final List<RoleHistory> values;

    private RoleHistories(List<RoleHistory> values) {
        this.values = values;
    }

    public RoleHistories() {
        this(new ArrayList<>());
    }

    public void add(RoleHistory roleHistory) {
        this.values.add(roleHistory);
    }

    public List<RoleHistory> getGroupByDate() {
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
