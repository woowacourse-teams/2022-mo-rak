package com.morak.back.appointment.domain.menu;

import static com.morak.back.core.exception.CustomErrorCode.DESCRIPTION_OUT_OF_LENGTH_ERROR;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void 설명의_길이가_1000자_이상인_경우_예외를_던진다() {
        // given
        String description = "ㅋ".repeat(1001);

        // when & then
        assertThatThrownBy(() -> new SubTitle(description))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(DESCRIPTION_OUT_OF_LENGTH_ERROR);
    }

    @Test
    void 설명은_공백이어도_된다() {
        // given
        String description = "";

        // when & then
        assertThatNoException().isThrownBy(() -> new SubTitle(description));
    }
}
