package com.morak.back.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
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

    @Test
    void Code의_길이가_8을_넘어가면_예외를_던진다() {
        // given
        String randomCode = "ㅋ".repeat(9);

        // when & then
        assertThatThrownBy(() -> Code.generate((length) -> randomCode))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.CODE_LENGTH_ERROR);
    }

}
