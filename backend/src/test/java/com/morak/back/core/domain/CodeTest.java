package com.morak.back.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeTest {

    @Test
    void Code를_생성한다() {
        // given
        String randomCode = "하이하이하이하이";

        //When
        Code code = Code.generate((length) -> randomCode);

        // then
        assertThat(code.getCode()).isEqualTo(randomCode);
    }
}