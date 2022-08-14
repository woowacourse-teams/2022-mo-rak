package com.morak.back.team.ui.dto;

import com.morak.back.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {

    private Long id;
    private String code;
    private String name;

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getCode(), team.getName());
    }
}
