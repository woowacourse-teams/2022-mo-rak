package com.morak.back.core.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalController {

    @GetMapping("/error-codes")
    public ResponseEntity<CodeDescriptor> toDocumentation() {
        return ResponseEntity.ok(new CodeDescriptor("code"));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class CodeDescriptor {
        private String code;
    }
}
