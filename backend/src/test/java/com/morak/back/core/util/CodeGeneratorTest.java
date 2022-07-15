package com.morak.back.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeGeneratorTest {

    @Test
    public void 랜덤_코드를_생성한다() {
        // given
        int length = 8;

        // when
        String code = CodeGenerator.createRandomCode(length);

        // then
        assertThat(code).hasSize(length);
    }
}
