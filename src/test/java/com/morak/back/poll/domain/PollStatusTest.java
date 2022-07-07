package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PollStatusTest {

    @DisplayName("투표가 종료되었는지 확인한다")
    @Test
    void isClosed() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        boolean isClosed = status.isClosed();
        // then
        assertThat(isClosed).isFalse();
    }

    @DisplayName("투표를 종료한다.")
    @Test
    void close() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        PollStatus closed = status.close();
        // then
        assertThat(closed).isSameAs(PollStatus.CLOSED);
    }

    @DisplayName("투표를 두 번 종료하면 예외를 던진다.")
    @Test
    void throwsExceptionOnClosingTwice() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        PollStatus closed = status.close();
        // then
        assertThatThrownBy(closed::close)
            .isInstanceOf(IllegalArgumentException.class);
    }
}