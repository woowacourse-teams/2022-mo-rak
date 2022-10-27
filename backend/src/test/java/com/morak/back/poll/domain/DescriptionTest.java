package com.morak.back.poll.domain;

import static com.morak.back.core.exception.CustomErrorCode.POLL_DESCRIPTION_LENGTH_ERROR;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.poll.exception.PollDomainLogicException;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void 설명의_길이가_1000자_이상인_경우_예외를_던진다() {
        // given
        String description = "ㅋ".repeat(1001);

        // when & then
        assertThatThrownBy(() -> new Description(description))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(POLL_DESCRIPTION_LENGTH_ERROR);
    }

    @Test
    void 설명은_공백이어도_된다() {
        // given
        String description = "";

        // when & then
        assertThatNoException().isThrownBy(() -> new Description(description));
    }
}
