package com.morak.back.auth.application;

public interface TokenProvider {

    String createToken(String payload);
}
