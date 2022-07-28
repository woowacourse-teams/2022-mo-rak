package com.morak.back.core.domain;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomCodeGenerator implements CodeGenerator {
    @Override
    public String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
