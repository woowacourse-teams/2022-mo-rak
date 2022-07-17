package com.morak.back.team.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/{teamCode}/invitation")
    public ResponseEntity<Void> createInvitationCode(@Auth Long memberId, @PathVariable String teamCode) {
        String code = teamService.createInvitationCode(memberId, teamCode);
        return ResponseEntity.created(URI.create("/groups/in/" + code)).build();
    }

    @GetMapping("/in/{invitationCode}")
    public ResponseEntity<InvitationJoinedResponse> isJoined(@Auth Long memberId, @PathVariable String invitationCode) {
        // TODO : group code는 header에 넣어줘야할까, body에 넣어줘야할까 ?
        // 기존까지는 redirection이 모두 Location header를 기반으로 했어서 ..
        return ResponseEntity.ok(teamService.isJoined(memberId, invitationCode));
    }

    @PostMapping("/in/{invitationCode}")
    public ResponseEntity<Void> joinTeam(@Auth Long memberId, @PathVariable String invitationCode) {
        String teamCode = teamService.join(memberId, invitationCode);
        return ResponseEntity.created(URI.create("/groups/" + teamCode)).build();
    }
}
