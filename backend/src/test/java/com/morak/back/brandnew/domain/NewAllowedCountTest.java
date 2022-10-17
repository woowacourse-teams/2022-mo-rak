package com.morak.back.brandnew.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NewAllowedCountTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 투표_허용_개수가_1보다_작으면_예외를_던진다(int allowedCount) {
        // when
        assertThatThrownBy(() -> new NewAllowedCount(allowedCount))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALLOWED_COUNT_MIN_ERROR);
    }
}
