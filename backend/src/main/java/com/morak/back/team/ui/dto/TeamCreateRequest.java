package com.morak.back.team.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public class TeamCreateRequest {
    private final String name;

    @JsonCreator
    public TeamCreateRequest(String name) {
        this.name = name;
    }
}
