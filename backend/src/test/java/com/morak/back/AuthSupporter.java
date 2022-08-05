package com.morak.back;

import java.util.Map;

public class AuthSupporter {

    public static Map<String, String> toHeader(String token) {
        return Map.of("Authorization", "Bearer " + token);
    }
}
