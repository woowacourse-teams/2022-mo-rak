package com.morak.back.team.ui;

import com.morak.back.auth.support.Auth;
import java.net.URI;

import javax.validation.Valid;
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
    public ResponseEntity<Void> createTeam(@Auth Long memberId, @Valid @RequestBody TeamCreateRequest request) {
        String code = teamService.createTeam(memberId, request);
        return ResponseEntity.created(URI.create("/groups/" + code)).build();
    }
}
