package com.morak.back.core.config;

import com.morak.back.appointment.domain.SystemTime;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    @Bean
    public SystemTime systemTime() {
        return new SystemTime(LocalDateTime.now());
    }
}
