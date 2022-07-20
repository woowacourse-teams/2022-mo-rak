package com.morak.back.team.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeamCreateRequest {

    @NotBlank
    private final String name;

    @JsonCreator
    public TeamCreateRequest(String name) {
        this.name = name;
    }
}
