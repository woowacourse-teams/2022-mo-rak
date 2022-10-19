package com.morak.back.appointment.domain.menu;

import static com.morak.back.core.exception.CustomErrorCode.TITLE_LENGTH_OUT_OF_RANGE_ERROR;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class TitleTest {

    @Test
    void 제목이_없는_경우_예외를_던진다() {
        // given
        String invalidTitle = "";

        // when & then
        assertThatThrownBy(() -> new Title(invalidTitle))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(TITLE_LENGTH_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 제목이_255자를_넘는_경우_예외를_던진다() {
        // given
        String invalidTitle = "ㅋ".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Title(invalidTitle))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(TITLE_LENGTH_OUT_OF_RANGE_ERROR);
    }
}
