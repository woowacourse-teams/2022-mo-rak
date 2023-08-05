package com.morak.back;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(EmbeddedRedisCallback.class)
class MorakBackApplicationTests {

    @Test
    void contextLoads() {
    }

}
