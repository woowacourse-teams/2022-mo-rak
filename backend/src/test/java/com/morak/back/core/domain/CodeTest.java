package com.morak.back.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CodeTest {
    @Test
    void Code를_생성한다() {
        // given
        String randomCode = "하이하이하이하이";

        // when
        Code code = Code.generate((length) -> randomCode);

        // then
        assertThat(code.getCode()).isEqualTo(randomCode);
    }

    @Test
    void 랜덤한_8자리의_Code를_생성한다() {
        // given
        RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

        // when
        Code code = Code.generate(randomCodeGenerator);

        // then
        assertThat(code.getCode()).matches("[\\dA-z]{8}");
    }
}
