package com.morak.back;

import com.morak.back.auth.application.FakeOAuthClient;
import com.morak.back.auth.application.OAuthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public OAuthClient getOauthClient() {
        return new FakeOAuthClient();
    }
}
