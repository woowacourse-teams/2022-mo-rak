package com.morak.back.team.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.morak.back.team.domain.Team;
import lombok.Getter;

@Getter
public class TeamResponse {

    private Long id;
    private String code;
    private String name;

    @JsonCreator
    public TeamResponse(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getCode(), team.getName());
    }
}
