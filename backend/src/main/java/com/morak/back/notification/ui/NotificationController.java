package com.morak.back.notification.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.notification.application.WebhookService;
import com.morak.back.notification.application.dto.SlackWebhookCreateRequest;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final WebhookService webhookService;

    @PostMapping("/api/groups/{groupCode}/slack")
    public ResponseEntity<Void> enrollSlackWebhookUrl(@PathVariable String groupCode,
                                                      @Auth Long memberId,
                                                      @RequestBody SlackWebhookCreateRequest request) {
        Long id = webhookService.saveSlackWebhook(groupCode, memberId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/slack/" + id)).build();
    }
}
