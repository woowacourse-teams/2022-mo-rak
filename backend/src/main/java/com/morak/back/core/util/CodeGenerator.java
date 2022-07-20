package com.morak.back.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CodeGenerator {

    public static String createRandomCode(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
