package com.morak.back.auth.ui;

import com.morak.back.auth.application.OAuthService;
import com.morak.back.auth.ui.dto.SigninResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/auth/signin")
    public String redirectToSignin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", clientId);
        return "redirect:" + GITHUB_LOGIN_URL;
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<SigninResponse> signin(@RequestParam String code) {
        return ResponseEntity.ok(oAuthService.signin(code));
    }
}
