package com.morak.back.team.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.morak.back.auth.domain.Team;
import lombok.Getter;

@Getter
public class TeamResponse {

    private String code;
    private String name;

    @JsonCreator
    public TeamResponse(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getCode(), team.getName());
    }
}
