package com.morak.back.appointment.domain.menu;

import static com.morak.back.core.exception.CustomErrorCode.PAST_CLOSED_TIME_ERROR;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.DomainLogicException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClosedAtTest {

    @Test
    void 마감시간이_현재보다_과거이면_예외를_던진다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime closedAt = now.minusMinutes(1);

        // when & then
        assertThatThrownBy(() -> new ClosedAt(closedAt, now))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(PAST_CLOSED_TIME_ERROR);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void 마감시간은_현재와_같거나_이후여야한다(int plusMinutes) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime closedAt = now.plusMinutes(plusMinutes);

        // when & then
        assertThatCode(() -> new ClosedAt(closedAt, now))
                .doesNotThrowAnyException();
    }
}
