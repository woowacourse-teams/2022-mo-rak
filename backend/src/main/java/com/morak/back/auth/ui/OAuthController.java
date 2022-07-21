package com.morak.back.auth.ui;

import com.morak.back.auth.application.OAuthService;
import com.morak.back.auth.ui.dto.SigninRequest;
import com.morak.back.auth.ui.dto.SigninResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class OAuthController {

    private static final String GITHUB_LOGIN_URL = "https://github.com/login/oauth/authorize";

    private final OAuthService oAuthService;
    private final String clientId;

    public OAuthController(OAuthService oAuthService,
                           @Value("${security.oauth.github.client-id}") String clientId) {
        this.oAuthService = oAuthService;
        this.clientId = clientId;
    }

    @PostMapping("/api/auth/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(oAuthService.signin(request));
    }
}
