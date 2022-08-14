package com.morak.back.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeTest {

    @Test
    void 랜덤한_8자리의_Code를_생성한다() {
        // given
        RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

        // when
        Code code = Code.generate(randomCodeGenerator);

        // then
        assertThat(code.getCode()).hasSize(8);
    }
}
