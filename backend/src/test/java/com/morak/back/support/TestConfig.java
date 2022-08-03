package com.morak.back.support;

import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    public SlackClient getSlackClient() {
        return new FakeSlackClient();
    }
}
