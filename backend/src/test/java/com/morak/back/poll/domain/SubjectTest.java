package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import org.junit.jupiter.api.Test;

class SubjectTest {

    @Test
    void 투표_항목_이름을_생성한다() {
        // given
        String subject = "엘리가 짱임!";

        // when & then
        assertDoesNotThrow(() -> new Subject(subject));
    }

    @Test
    void 투표_항목_이름의_길이가_255를_넘으면_예외를_던진다() {
        // given
        String subject = "해리".repeat(128);

        // when & then
        assertThatThrownBy(() -> new Subject(subject))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_SUBJECT_LENGTH_ERROR);
    }
}
