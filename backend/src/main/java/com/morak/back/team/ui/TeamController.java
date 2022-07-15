package com.morak.back.team.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.dto.TeamCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> createTeam(@RequestBody TeamCreateRequest request) {
        Long id = teamService.createTeam(request);

        return ResponseEntity.created(URI.create("/groups/" + id)).build();
    }
}
