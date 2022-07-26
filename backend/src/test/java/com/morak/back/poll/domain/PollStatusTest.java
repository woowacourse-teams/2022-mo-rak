package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

class PollStatusTest {

    @Test
    void 투표가_종료되었는지_확인한다() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        boolean isClosed = status.isClosed();
        // then
        assertThat(isClosed).isFalse();
    }

    @Test
    void 투표를_종료한다() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        PollStatus closed = status.close();
        // then
        assertThat(closed).isSameAs(PollStatus.CLOSED);
    }

    @Test
    void 투표를_두_번_종료하면_예외를_던진다() {
        // given
        PollStatus status = PollStatus.OPEN;
        // when
        PollStatus closed = status.close();
        // then
        assertThatThrownBy(closed::close)
            .isInstanceOf(InvalidRequestException.class);
    }
}
