package com.morak.back.auth.application;

public interface TokenProvider {

    String createToken(String payload);

    String parsePayload(String token);

    void validateToken(String token);
}
