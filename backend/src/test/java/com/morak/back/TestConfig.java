package com.morak.back;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.auth.application.FakeOAuthClient;
import com.morak.back.auth.application.OAuthClient;
import com.morak.back.support.FakeBean;
import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public OAuthClient getOauthClient() {
        return new FakeOAuthClient();
    }

//    @FakeBean
//    @Bean
//    public SystemTime getSystemTime() {
//        return new SystemTime(LocalDateTime.now());
//    }
}
