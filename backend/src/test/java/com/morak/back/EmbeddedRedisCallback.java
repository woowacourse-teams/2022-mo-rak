package com.morak.back;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import redis.embedded.RedisServer;

public class EmbeddedRedisCallback implements BeforeAllCallback, AfterAllCallback {

    private static final int REDIS_PORT = 6379;
    private RedisServer redisServer;

    @Override
    public void beforeAll(ExtensionContext context) {
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
