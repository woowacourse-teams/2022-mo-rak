package com.morak.back.team.application;

import org.springframework.stereotype.Service;

import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.team.ui.dto.TeamCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final int CODE_LENGTH = 8;

    private final TeamRepository teamRepository;

    public Long createTeam(TeamCreateRequest request) {
        Team team = new Team(null, request.getName(), CodeGenerator.createRandomCode(CODE_LENGTH));

        Team savedTeam = teamRepository.save(team);
        return savedTeam.getId();
    }
}
