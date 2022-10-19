package com.morak.back.appointment.config;

import com.morak.back.appointment.domain.SystemTime;
import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AppointmentConfig {

    @Bean
    public SystemTime systemTime() {
        return new SystemTime(LocalDateTime.of(2022, 10, 19, 14, 17, 0));
    }

}
