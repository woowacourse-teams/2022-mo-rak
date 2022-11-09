package com.morak.back.auth.ui;

import com.morak.back.auth.application.OAuthService;
import com.morak.back.auth.support.Auth;
import com.morak.back.auth.application.dto.ChangeNameRequest;
import com.morak.back.auth.application.dto.MemberResponse;
import com.morak.back.auth.application.dto.SigninRequest;
import com.morak.back.auth.application.dto.SigninResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oAuthService.signin(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMe(@Auth Long memberId) {
        return ResponseEntity.ok(oAuthService.findMember(memberId));
    }

    @PatchMapping("/me/name")
    public ResponseEntity<Void> changeName(@Auth Long memberId, @Valid @RequestBody ChangeNameRequest request) {
        oAuthService.changeName(memberId, request);
        return ResponseEntity.ok().build();
    }
}
