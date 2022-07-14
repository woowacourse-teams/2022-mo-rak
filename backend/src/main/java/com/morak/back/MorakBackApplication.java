package com.morak.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MorakBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MorakBackApplication.class, args);
    }

}
